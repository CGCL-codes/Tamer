package org.netxilia.api.impl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.netxilia.api.display.IStyleService;
import org.netxilia.api.event.IWorkbookEventListener;
import org.netxilia.api.event.SheetEvent;
import org.netxilia.api.event.SheetEventType;
import org.netxilia.api.exception.AlreadyExistsException;
import org.netxilia.api.exception.NotFoundException;
import org.netxilia.api.exception.StorageException;
import org.netxilia.api.impl.NetxiliaSystemImpl;
import org.netxilia.api.impl.dependencies.WorkbookAliasDependencyManager;
import org.netxilia.api.impl.dependencies.WorkbookDependencyManager;
import org.netxilia.api.model.ISheet;
import org.netxilia.api.model.IWorkbook;
import org.netxilia.api.model.SheetData;
import org.netxilia.api.model.SheetFullName;
import org.netxilia.api.model.SheetType;
import org.netxilia.api.model.WorkbookId;
import org.netxilia.api.user.IAclService;
import org.netxilia.api.user.IUserService;
import org.netxilia.api.user.Permission;
import org.netxilia.api.value.IGenericValueParseService;
import org.netxilia.spi.formula.IFormulaParser;
import org.springframework.util.Assert;

/**
 * A workbook represents a collection of Sheets. It should be seen in a larger wy than a Excel workbook as is meant to
 * contain many sheets.
 * 
 * The access to sheets is synchronized.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class Workbook implements Serializable, IWorkbook {

    private static final long serialVersionUID = -2496417506141917109L;

    private final WorkbookId id;

    private final Map<String, ISheet> sheetsByName;

    private String name;

    private final NetxiliaSystemImpl workbookProcessor;

    private WorkbookDependencyManager dependencyManager;

    private WorkbookAliasDependencyManager aliasDependencyManager;

    private final WorkbookEventSupport eventSupport;

    public static Workbook newInstance(NetxiliaSystemImpl workbookProcessor, WorkbookId workbookId) throws StorageException, NotFoundException {
        return newInstance(workbookProcessor, workbookId, new ArrayList<SheetData>());
    }

    public static Workbook newInstance(NetxiliaSystemImpl workbookProcessor, WorkbookId workbookId, List<SheetData> sheets) throws StorageException, NotFoundException {
        Workbook workbook = new Workbook(workbookProcessor, workbookId, sheets);
        workbook.dependencyManager = WorkbookDependencyManager.newInstance(workbook, workbookProcessor.getMoreCellCommands(), workbookProcessor.getFormulaParser());
        workbook.aliasDependencyManager = WorkbookAliasDependencyManager.newInstance(workbook, workbookProcessor.getFormulaParser(), workbookProcessor.getMoreCellCommands());
        return workbook;
    }

    private Workbook(NetxiliaSystemImpl workbookProcessor, WorkbookId workbookId, List<SheetData> sheets) throws StorageException, NotFoundException {
        Assert.notNull(workbookProcessor);
        Assert.notNull(workbookId);
        Assert.notNull(sheets);
        this.id = workbookId;
        this.name = id.getKey();
        eventSupport = new WorkbookEventSupport();
        this.workbookProcessor = workbookProcessor;
        this.sheetsByName = new HashMap<String, ISheet>();
        for (SheetData sheetData : sheets) {
            sheetsByName.put(sheetData.getFullName().getSheetName(), createSheetActorRef(sheetData));
        }
    }

    public WorkbookId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * 
	 * @return if the sheet must be initialized on open - used for tests only
	 */
    public boolean isInitializationEnabled() {
        return workbookProcessor.isSheetInitializationEnabled();
    }

    public synchronized ISheet getSheet(String sheetName) throws NotFoundException {
        getAclService().checkPermission(id, Permission.read);
        ISheet sheet = sheetsByName.get(sheetName);
        if (sheet == null) {
            throw new NotFoundException("Sheet named " + sheetName + " was not found in workbook:" + name);
        }
        return sheet;
    }

    public synchronized boolean hasSheet(String sheetName) {
        getAclService().checkPermission(id, Permission.read);
        return sheetsByName.containsKey(sheetName);
    }

    private IAclService getAclService() {
        return workbookProcessor.getAclService();
    }

    public void addListener(IWorkbookEventListener listener) {
        eventSupport.removeListener(listener);
        eventSupport.addListener(listener);
    }

    public void removeListener(IWorkbookEventListener listener) {
        eventSupport.removeListener(listener);
    }

    /**
	 * 
	 * @param mainSheetName
	 * @return all the "user" sheets linked to the given sheet name
	 */
    public synchronized Collection<ISheet> getUserSheets(String mainSheetName) {
        List<ISheet> userSheets = new ArrayList<ISheet>();
        SheetFullName summarySheetName = SheetFullName.summarySheetName(new SheetFullName(name, mainSheetName), null);
        for (ISheet sheet : sheetsByName.values()) {
            if (!sheet.getName().equals(summarySheetName.getSheetName()) && sheet.getName().startsWith(mainSheetName + ".") && sheet.getType() == SheetType.user) {
                userSheets.add(sheet);
            }
        }
        return userSheets;
    }

    @Override
    public synchronized Collection<ISheet> getSheets() {
        getAclService().checkPermission(id, Permission.read);
        return new ArrayList<ISheet>(sheetsByName.values());
    }

    public synchronized ISheet addNewSheet(String sheetName, SheetType type) throws AlreadyExistsException, StorageException, NotFoundException {
        getAclService().checkPermission(id, Permission.write);
        if ((sheetName == null) || sheetName.isEmpty()) {
            throw new IllegalArgumentException("The sheet has to have a non-empty name");
        }
        if (sheetsByName.containsKey(sheetName)) {
            throw new AlreadyExistsException("A sheet with the name [" + sheetName + "] already exists in this workbook");
        }
        SheetData sheetData = new SheetData(new SheetFullName(id, sheetName), type);
        ISheet sheet = createSheetActorRef(sheetData);
        sheetsByName.put(sheetName, sheet);
        getAclService().setPermissions(sheetData.getFullName(), getUserService().getCurrentUser(), Permission.read, Permission.write);
        eventSupport.fireEvent(new SheetEvent(SheetEventType.inserted, sheetData, Collections.<SheetData.Property>emptyList()));
        return sheet;
    }

    /**
	 * deletes the sheet with the given name
	 * 
	 * @param sheetName
	 * @throws NotFoundException
	 *             if the given sheet does not exist in this workbook
	 */
    public synchronized void deleteSheet(ISheet sheet) throws NotFoundException {
        if (sheetsByName.remove(sheet.getName()) != null) {
            workbookProcessor.getStorageService().deleteSheet(sheet.getFullName(), sheet.getType());
            eventSupport.fireEvent(new SheetEvent(SheetEventType.deleted, new SheetData(new SheetFullName(id, sheet.getName()), sheet.getType()), Collections.<SheetData.Property>emptyList()));
        }
    }

    @Override
    public synchronized void deleteSheet(String sheetFullName) throws NotFoundException, StorageException {
        ISheet sheetDesc = getSheet(sheetFullName);
        getAclService().checkPermission(SheetFullName.mainSheetName(new SheetFullName(id, sheetDesc.getName()), getUserService().getCurrentUser()), Permission.write);
        if (sheetDesc.getType() == SheetType.normal) {
            ISheet summarySheet = sheetsByName.get(SheetFullName.summarySheetName(sheetDesc, null).getSheetName());
            if (summarySheet != null) {
                deleteSheet(summarySheet);
            }
            Collection<ISheet> userSheets = getUserSheets(sheetFullName);
            for (ISheet userSheet : userSheets) {
                deleteSheet(userSheet);
            }
        }
        deleteSheet(sheetDesc);
    }

    public WorkbookDependencyManager getDependencyManager() {
        return dependencyManager;
    }

    public synchronized void removeAllSheets() {
        sheetsByName.clear();
    }

    private IUserService getUserService() {
        return workbookProcessor.getUserService();
    }

    public IFormulaParser getFormulaParser() {
        return workbookProcessor.getFormulaParser();
    }

    IStyleService getStyleService() {
        return workbookProcessor.getStyleService();
    }

    IGenericValueParseService getParseService() {
        return workbookProcessor.getParseService();
    }

    NetxiliaSystemImpl getWorkbookProcessor() {
        return workbookProcessor;
    }

    public WorkbookAliasDependencyManager getAliasDependencyManager() {
        return aliasDependencyManager;
    }

    protected ISheet createSheetActorRef(SheetData sheetData) throws StorageException, NotFoundException {
        SheetActor actor = new SheetActor(this, sheetData, workbookProcessor.getFormulaCalculatorFactory(), getFormulaParser(), workbookProcessor.getStorageService().getSheetStorage(sheetData.getFullName(), sheetData.getType()), workbookProcessor.getExecutorServiceFactory(), workbookProcessor.getSpringUserService(), workbookProcessor.getMoreCellCommands(), workbookProcessor.getPreloadContextFactory());
        SheetProxy proxy = new SheetProxy(workbookProcessor.getExecutorServiceFactory().newExecutorService(sheetData.getFullName().toString()), workbookProcessor.getSpringUserService(), workbookProcessor.getAclService(), actor);
        actor.setActorRef(proxy);
        return proxy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("workbook", id).append("name", name).append("sheetsSize", sheetsByName.size()).toString();
    }
}
