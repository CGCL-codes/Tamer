package org.gvt.gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.gvt.util.EntityHolder;
import java.util.Arrays;
import java.util.*;

/**
 * This class maintains the Add Entity Dialog which is used for
 * adding entities during local querying
 *
 * @author Ozgun Babur
 * @author Merve Cakir
 * @author Shatlyk Ashyralyev
 * 
 * Copyright: I-Vis Research Group, Bilkent University, 2007
 */
public class AddEntityDialog extends Dialog {

    /**
	 * Result of filtering physical Entities with searchKey
	 */
    private ArrayList<EntityHolder> possibleEntities;

    /**
	 * Selected physical Entities
	 */
    private ArrayList<EntityHolder> selectedEntities;

    /**
	 * All physical Entites
	 */
    private ArrayList<EntityHolder> allEntities;

    /**
	 * Provides a mapping from KeyName of the entity to the entity type.
	 */
    private Map<String, EntityHolder> entityKeyNameMap;

    /**
	 * Boolean to check whether add button or cancel button has been pressed
	 */
    private boolean addPressed;

    /**
	 * SearchKey Text and Filter Button, to filter entities
	 */
    private Text searchKey;

    private Button filterButton;

    /**
	 * Entity List
	 */
    private List entityList;

    private Set<String> possibleEntityNames;

    private String[] allEntityNames;

    /**
	 * Buttons to add entities, showAll entities, cancel
	 */
    private Button addButton;

    private Button cancelButton;

    private Button showAllButton;

    /**
	 * Shell for dialog
	 */
    private Shell shell;

    /**
	 * Create the dialog
	 */
    public AddEntityDialog(Shell shell, final ArrayList<EntityHolder> allEntities) {
        super(shell);
        this.entityKeyNameMap = new HashMap<String, EntityHolder>();
        this.selectedEntities = new ArrayList<EntityHolder>();
        this.possibleEntities = new ArrayList<EntityHolder>();
        this.possibleEntityNames = new HashSet<String>();
        for (EntityHolder entity : allEntities) {
            String keyName = entity.getName();
            possibleEntityNames.add(keyName);
            entityKeyNameMap.put(keyName, entity);
            possibleEntities.add(entity);
        }
        this.allEntityNames = toSortedArray(possibleEntityNames);
        this.allEntities = allEntities;
        this.shell = shell;
        addPressed = false;
    }

    /**
	 * keyAdapter for TextFiled
	 */
    protected KeyAdapter keyAdapter = new KeyAdapter() {
    };

    /**
	 * Open the dialog
	 */
    public boolean open() {
        createContents();
        shell.setLocation(getParent().getLocation().x + (getParent().getSize().x / 2) - (shell.getSize().x / 2), getParent().getLocation().y + (getParent().getSize().y / 2) - (shell.getSize().y / 2));
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return addPressed;
    }

    /**
	 * Create contents of the dialog
	 */
    protected void createContents() {
        shell = new Shell(getParent(), SWT.RESIZE | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText("Add Entity");
        ButtonAdapter adapter = new ButtonAdapter();
        ImageDescriptor id = ImageDescriptor.createFromFile(NeighborhoodQueryParamWithEntitiesDialog.class, "/org/gvt/icon/cbe-icon.png");
        shell.setImage(id.createImage());
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        shell.setLayout(gridLayout);
        searchKey = new Text(shell, SWT.BORDER);
        searchKey.addKeyListener(keyAdapter);
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 2;
        gridData.minimumWidth = 200;
        searchKey.setLayoutData(gridData);
        filterButton = new Button(shell, SWT.NONE);
        filterButton.setText("Filter");
        filterButton.addSelectionListener(adapter);
        gridData = new GridData(GridData.FILL, GridData.CENTER, false, false);
        filterButton.setLayoutData(gridData);
        entityList = new List(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        entityList.setItems(allEntityNames);
        gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
        gridData.verticalSpan = 20;
        gridData.horizontalSpan = 3;
        gridData.heightHint = entityList.getItemHeight() * 10;
        gridData.widthHint = 500;
        entityList.setLayoutData(gridData);
        showAllButton = new Button(shell, SWT.NONE);
        showAllButton.setText("Show All");
        showAllButton.addSelectionListener(adapter);
        gridData = new GridData(GridData.CENTER, GridData.CENTER, false, false);
        showAllButton.setLayoutData(gridData);
        addButton = new Button(shell, SWT.NONE);
        addButton.setText("Add");
        addButton.addSelectionListener(adapter);
        gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
        gridData.minimumWidth = 100;
        gridData.horizontalIndent = 5;
        addButton.setLayoutData(gridData);
        cancelButton = new Button(shell, SWT.NONE);
        cancelButton.setText("Cancel");
        cancelButton.addSelectionListener(adapter);
        gridData = new GridData(GridData.CENTER, GridData.CENTER, false, false);
        gridData.horizontalIndent = 5;
        cancelButton.setLayoutData(gridData);
        shell.pack();
    }

    /**
	 * Filters entities in List according to searchKey
	 */
    public void filterEntities() {
        String searchKeyWord = this.searchKey.getText();
        this.selectedEntities.clear();
        if (searchKeyWord == null) {
            this.possibleEntities = new ArrayList<EntityHolder>(this.allEntities);
            return;
        }
        this.possibleEntities.clear();
        this.possibleEntityNames.clear();
        for (EntityHolder entity : allEntities) {
            String name = entity.getName();
            if (entity.containsWord(searchKeyWord)) {
                this.possibleEntities.add(entity);
                this.possibleEntityNames.add(name);
            }
        }
    }

    /**
	 * This method stores selected entities in selectedEntities List
	 */
    private void saveSelectedEntities() {
        this.selectedEntities.clear();
        String[] selectionResult = entityList.getSelection();
        for (String selected : selectionResult) {
            for (EntityHolder entity : possibleEntities) {
                if (selected != null && selected.equals(entity.getName())) {
                    this.selectedEntities.add(entity);
                }
            }
        }
    }

    /**
	 * Getter
	 */
    public ArrayList<EntityHolder> getSelectedEntities() {
        return selectedEntities;
    }

    /**
	 * Method which sorts given set of Strings and converts them to Array
	 */
    private String[] toSortedArray(Set<String> entityNames) {
        String[] entityNameArray = new String[entityNames.size()];
        entityNameArray = entityNames.toArray(entityNameArray);
        Arrays.sort(entityNameArray);
        return entityNameArray;
    }

    /**
	 * Shows all entities in entity list
	 */
    private void showAllEntities() {
        this.possibleEntities = new ArrayList<EntityHolder>(this.allEntities);
        this.selectedEntities.clear();
        this.entityList.removeAll();
        this.entityList.setItems(allEntityNames);
        searchKey.setText("");
    }

    /**
	 * This method updates the entity list. 
	 * It is called just after the filter
	 */
    private void updateList() {
        this.entityList.removeAll();
        this.entityList.setItems(toSortedArray(this.possibleEntityNames));
    }

    /**
	 * Class for handling button selections of buttons
	 */
    class ButtonAdapter extends SelectionAdapter {

        public void widgetSelected(SelectionEvent arg) {
            Button button = (Button) arg.widget;
            if (button == addButton) {
                saveSelectedEntities();
                addPressed = true;
                shell.dispose();
            } else if (button == cancelButton) {
                addPressed = false;
                shell.dispose();
            } else if (button == filterButton) {
                filterEntities();
                updateList();
            } else if (button == showAllButton) {
                showAllEntities();
            }
        }
    }
}
