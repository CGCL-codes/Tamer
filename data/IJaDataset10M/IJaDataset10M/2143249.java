package org.netxilia.spi.impl.storage.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netxilia.api.exception.AlreadyExistsException;
import org.netxilia.api.exception.NotFoundException;
import org.netxilia.api.exception.StorageException;
import org.netxilia.api.formula.Formula;
import org.netxilia.api.impl.NetxiliaSystemImpl;
import org.netxilia.api.impl.model.Workbook;
import org.netxilia.api.model.CellData;
import org.netxilia.api.model.IWorkbook;
import org.netxilia.api.model.SheetData;
import org.netxilia.api.model.SheetFullName;
import org.netxilia.api.model.SheetType;
import org.netxilia.api.model.WorkbookId;
import org.netxilia.api.reference.CellReference;
import org.netxilia.api.storage.DataSourceConfigurationId;
import org.netxilia.api.utils.Matrix;
import org.netxilia.api.value.IGenericValueParseService;
import org.netxilia.spi.storage.ISheetStorageService;
import org.netxilia.spi.storage.IWorkbookStorageService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class InMemoryWorkbookStorageServiceImpl implements IWorkbookStorageService, InitializingBean, ApplicationContextAware {

    private Map<WorkbookId, Workbook> workbooks = new HashMap<WorkbookId, Workbook>();

    private Map<SheetFullName, InMemorySheetStorageServiceImpl> sheets = new HashMap<SheetFullName, InMemorySheetStorageServiceImpl>();

    private Map<String, String> values;

    private ApplicationContext context;

    private NetxiliaSystemImpl workbookProcessor;

    @Autowired
    private IGenericValueParseService parseService;

    public NetxiliaSystemImpl getWorkbookProcessor() {
        return workbookProcessor;
    }

    public void setWorkbookProcessor(NetxiliaSystemImpl workbookProcessor) {
        this.workbookProcessor = workbookProcessor;
    }

    public IGenericValueParseService getParseService() {
        return parseService;
    }

    public void setParseService(IGenericValueParseService parseService) {
        this.parseService = parseService;
    }

    @Override
    public synchronized void deleteWorkbook(WorkbookId workbookId) throws StorageException {
        Workbook wk = workbooks.remove(workbookId.getKey());
        if (wk == null) {
            throw new StorageException("Unknown workbook:" + workbookId);
        }
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    private CellData fromString(CellReference ref, String theValue) {
        if (Formula.isFormula(theValue)) {
            return new CellData(ref, new Formula(theValue));
        } else {
            return new CellData(ref, parseService.parse(theValue));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        workbookProcessor = context.getBean(NetxiliaSystemImpl.class);
        if (values == null) {
            return;
        }
        for (Map.Entry<String, String> entry : values.entrySet()) {
            CellReference ref = new CellReference(entry.getKey());
            SheetFullName sheetName = new SheetFullName(ref.getSheetName());
            ref = ref.withSheetName(sheetName.getSheetName());
            InMemorySheetStorageServiceImpl sheetStorage = sheets.get(sheetName);
            if (sheetStorage == null) {
                CompleteSheet sheet = new CompleteSheet();
                sheet.setSheetData(new SheetData(sheetName, SheetType.normal));
                sheet.setCells(new Matrix<CellData>());
                sheetStorage = new InMemorySheetStorageServiceImpl(sheet);
                sheets.put(sheetName, sheetStorage);
            }
            sheetStorage.getSheet().setCells(sheetStorage.getSheet().getCellsBuilder().set(ref.getRowIndex(), ref.getColumnIndex(), fromString(ref, entry.getValue())).build());
            InMemorySheetStorageServiceImpl.minRows(sheetStorage.getSheet().getRows(), ref.getRowIndex() + 1);
            InMemorySheetStorageServiceImpl.minColumns(sheetStorage.getSheet().getColumns(), ref.getColumnIndex() + 1);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public synchronized IWorkbook add(DataSourceConfigurationId dataSourceConfigId, WorkbookId workbookId) throws StorageException, NotFoundException, AlreadyExistsException {
        if (workbooks.get(workbookId) != null) {
            throw new AlreadyExistsException();
        }
        Workbook wk = Workbook.newInstance(workbookProcessor, workbookId);
        workbooks.put(workbookId, wk);
        return wk;
    }

    @Override
    public synchronized List<SheetData> loadSheets(WorkbookId workbookName) throws StorageException, NotFoundException {
        List<SheetData> sheetData = new ArrayList<SheetData>();
        for (InMemorySheetStorageServiceImpl s : this.sheets.values()) {
            if (workbookName.equals(s.getSheet().getSheetData().getFullName().getWorkbookId())) {
                sheetData.add(s.getSheet().getSheetData());
            }
        }
        return sheetData;
    }

    @Override
    public ISheetStorageService getSheetStorage(SheetFullName sheetName, SheetType sheetType) throws StorageException, NotFoundException {
        InMemorySheetStorageServiceImpl sheetStorage = sheets.get(sheetName);
        if (sheetStorage == null) {
            CompleteSheet sheet = new CompleteSheet();
            sheet.setSheetData(new SheetData(sheetName, sheetType));
            sheetStorage = new InMemorySheetStorageServiceImpl(sheet);
            sheets.put(sheetName, sheetStorage);
        }
        return sheetStorage;
    }

    @Override
    public void deleteSheet(SheetFullName sheetName, SheetType sheetType) throws StorageException, NotFoundException {
        sheets.remove(sheetName);
    }
}
