package com.piece_framework.yaml_editor.ui.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import com.piece_framework.yaml_editor.plugin.ConfigurationFactory;
import com.piece_framework.yaml_editor.plugin.IConfiguration;
import com.piece_framework.yaml_editor.plugin.IYAMLEditor;
import com.piece_framework.yaml_editor.plugin.Messages;
import com.piece_framework.yaml_editor.plugin.YAMLEditorPlugin;
import com.piece_framework.yaml_editor.ui.dialog.SchemaFolderSelectionDialog;

/**
 * YAML テキストエディター(スキーマ選択コンボボックス付).
 * YAMLEditor クラスが提供するエディター機能とスキーマ選択
 * コンボボックスを表示・管理する。
 * 
 * @author Hideharu Matsufuji
 * @version 0.1.0
 * @since 0.1.0
 * @see org.eclipse.ui.part.EditorPart
 * @see YAMLEditor
 * @see org.eclipse.core.resources.IResourceChangeListener
 */
public class YAMLEditorPart extends EditorPart implements IResourceChangeListener, IYAMLEditor {

    private static final int SCHEMA_COMBO_WIDTH = 200;

    private static final int SCHEMA_COMBO_HEIGHT = 50;

    private static final int SCHEMA_LABEL_WIDTH = 200;

    private static final int SCHEMA_LABEL_HEIGHT = 15;

    private static final int SCHEMA_GROUP_LEFT = 5;

    private static final int SCHEMA_GROUP_RIGHT = 5;

    private static final int SCHEMA_GROUP_SPACING = 5;

    private static final RGB SCHEMA_LABEL_NORM_COLOR = new RGB(0, 0, 255);

    private static final RGB SCHEMA_LABEL_ERR_COLOR = new RGB(255, 0, 0);

    private static final RGB SCHEMA_LABEL_FORE_COLOR = new RGB(255, 255, 255);

    private YAMLEditor fEditor;

    private Combo fSchemaCombo;

    private Label fSchemaFolderLabel;

    private Button fSchemaFolderButton;

    private IConfiguration fConfig;

    /**
     * コンストラクタ.
     * YAML Editor の生成及び各初期化を行う。
     *
     */
    public YAMLEditorPart() {
        super();
        fEditor = new YAMLEditor();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
    }

    /**
     * テキストを保存するときの処理を行う.
     * YAMLEditor クラスの doSave メソッドを呼出す。
     * 
     * @param monitor プログレスモニタ
     * @see YAMLEditor#doSave(IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor monitor) {
        saveYAMLFile();
        fEditor.doSave(monitor);
    }

    /**
     * ファイル名を指定してテキストを保存するときの処理を行う.
     * YAMLEditor クラスの doSaveAs メソッドを呼出す。
     * 
     * @see YAMLEditor#doSaveAs()
     */
    @Override
    public void doSaveAs() {
        saveYAMLFile();
        fEditor.doSaveAs();
    }

    /**
     * 初期化を行う.
     * 
     * @param site エディターサイト
     * @param input エディターインプット
     * @throws PartInitException 初期化時例外
     */
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        setPartName(input.getName());
    }

    /**
     * 終了処理を行う.
     */
    @Override
    public void dispose() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
        super.dispose();
    }

    /**
     * 最終保存時から変更されたかを返す.
     * YAMLEditor クラスの doSaveAs メソッドを呼出す。
     * 
     * @return 最終保存時から変更されたか
     * @see YAMLEditor#isDirty()()
     */
    @Override
    public boolean isDirty() {
        return fEditor.isDirty();
    }

    /**
     * テキストを保存できるかを返す.
     * 
     * @return テキストを保存できるか
     */
    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
     * 各コントロールの生成・配置を行う.
     * コントロールのサイズ修正は ControlListener イベントを利用する。
     * 
     * @param parent 親コントロール
     */
    @Override
    public void createPartControl(Composite parent) {
        try {
            RowLayout parentLayout = new RowLayout(SWT.VERTICAL);
            parentLayout.marginRight = 0;
            parentLayout.marginLeft = 0;
            parentLayout.marginTop = 0;
            parentLayout.marginBottom = 0;
            parentLayout.marginHeight = 0;
            parentLayout.marginWidth = 0;
            final Composite parentComposite = new Composite(parent, SWT.NONE);
            parentComposite.setLayout(parentLayout);
            RowLayout schemaLayout = new RowLayout(SWT.HORIZONTAL);
            schemaLayout.marginRight = SCHEMA_GROUP_RIGHT;
            schemaLayout.marginLeft = SCHEMA_GROUP_LEFT;
            schemaLayout.marginTop = 0;
            schemaLayout.marginBottom = 0;
            schemaLayout.marginHeight = 0;
            schemaLayout.marginWidth = 0;
            schemaLayout.spacing = SCHEMA_GROUP_SPACING;
            Group schemaGroup = new Group(parentComposite, SWT.NONE);
            schemaGroup.setText(Messages.getString("YAMLEditorPart.SchemaTitle"));
            schemaGroup.setLayout(schemaLayout);
            fSchemaCombo = new Combo(schemaGroup, SWT.READ_ONLY);
            fSchemaCombo.setLayoutData(new RowData(SCHEMA_COMBO_WIDTH, SCHEMA_COMBO_HEIGHT));
            fSchemaFolderLabel = new Label(schemaGroup, SWT.BORDER);
            fSchemaFolderLabel.setLayoutData(new RowData(SCHEMA_LABEL_WIDTH, SCHEMA_LABEL_HEIGHT));
            fSchemaFolderButton = new Button(schemaGroup, SWT.NONE);
            fSchemaFolderButton.setText(Messages.getString("YAMLEditorPart.ChangeSchemaFolderButtonCaption"));
            fSchemaFolderButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    changeSchemaFolder();
                    super.widgetSelected(e);
                }
            });
            FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
            fillLayout.marginHeight = 0;
            fillLayout.marginWidth = 0;
            fillLayout.spacing = 0;
            final Composite editorComposite = new Composite(parentComposite, SWT.NONE);
            editorComposite.setLayout(fillLayout);
            fEditor.init(getEditorSite(), getEditorInput());
            fEditor.addPropertyListener(new IPropertyListener() {

                public void propertyChanged(Object source, int propertyId) {
                    firePropertyChange(propertyId);
                }
            });
            fEditor.createPartControl(editorComposite);
            parent.addControlListener(new ControlListener() {

                public void controlMoved(ControlEvent e) {
                }

                public void controlResized(ControlEvent e) {
                    Point parentSize = parentComposite.getParent().getSize();
                    editorComposite.setLayoutData(new RowData(parentSize.x, parentSize.y - SCHEMA_COMBO_HEIGHT));
                }
            });
        } catch (PartInitException e) {
            YAMLEditorPlugin.showDialog(IStatus.ERROR, e);
        }
        initYAMLSchema();
    }

    /**
     * フォーカスを取得したときの処理を行う.
     * 
     */
    @Override
    public void setFocus() {
        fEditor.setFocus();
    }

    /**
     * リソースが変更された場合の処理を行う.
     * 
     * @param event リソース変更イベント
     */
    public void resourceChanged(IResourceChangeEvent event) {
        if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    String yamlFileName = getYAMLFile();
                    IFile yamlFile = getYAMLProject().getFile(yamlFileName);
                    if (yamlFile != null) {
                        if (!yamlFile.exists()) {
                            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                            page.closeEditor(YAMLEditorPart.this, false);
                        } else if (!getPartName().equals(yamlFile.getName())) {
                            setPartName(yamlFile.getName());
                        }
                    }
                }
            });
        }
    }

    /**
     * 指定されたアダプターに対してオブジェクトを返す.
     * 
     * @param adapter アダプター
     * @return オブジェクト
     */
    @Override
    public Object getAdapter(Class adapter) {
        return fEditor.getAdapter(adapter);
    }

    /**
     * プラグインからの設定の変更通知を受け取る.
     * 
     * @see com.piece_framework.yaml_editor.plugin.IYAMLEditor#changeProperty()
     */
    public void changeProperty() {
        IResource resource = (IResource) getEditorInput().getAdapter(IResource.class);
        try {
            resource.deleteMarkers(null, true, IResource.DEPTH_ZERO);
        } catch (CoreException e) {
        }
        initYAMLSchema();
    }

    /**
     * スキーマフォルダーを変更する.
     * 
     */
    public void changeSchemaFolder() {
        SchemaFolderSelectionDialog dialog = new SchemaFolderSelectionDialog(null, getYAMLProject());
        if (dialog.open() == Window.OK) {
            IFolder schemaFolder = dialog.getSelectedSchemaFolder();
            if (schemaFolder != null) {
                String tmp = schemaFolder.getFullPath().toString();
                int st = tmp.indexOf('/', 1);
                String schemaFolderName = tmp.substring(st);
                if (!schemaFolderName.equals(fConfig.get(IConfiguration.KEY_SCHEMAFOLDER))) {
                    String[] keys = fConfig.getKeys();
                    for (int i = 0; i < keys.length; i++) {
                        if (keys[i].startsWith(IConfiguration.KEY_PREFIX_SCHEMAFILE)) {
                            fConfig.remove(keys[i]);
                        }
                    }
                    fConfig.set(IConfiguration.KEY_SCHEMAFOLDER, schemaFolderName);
                    fConfig.store();
                    YAMLEditorPlugin.getDefault().notifyPropertyChanged();
                }
            }
        }
    }

    /**
     * スキーマ関連の初期化処理を行う.
     * 
     */
    private void initYAMLSchema() {
        if (fConfig == null) {
            fConfig = ConfigurationFactory.getConfiguration(getYAMLProject());
        }
        if (fConfig != null) {
            setSchemaFolder();
            setSchemaFileList();
        } else {
            fSchemaCombo.setEnabled(false);
            fSchemaFolderLabel.setText(Messages.getString("YAMLEditorPart.NoProject"));
            fSchemaFolderButton.setEnabled(false);
        }
    }

    /**
     * 編集中の YAML ファイルが所属するプロジェクトを返す.
     *  
     * @return プロジェクト
     */
    private IProject getYAMLProject() {
        IProject project = null;
        if (getEditorInput() != null && getEditorInput() instanceof IFileEditorInput) {
            IFile yamlFile = ((IFileEditorInput) getEditorInput()).getFile();
            project = yamlFile.getProject();
        }
        return project;
    }

    /**
     * 編集中の YAML ファイルを返す.
     * 先頭のプロジェクト名はカットする。
     * 
     * @return YAML ファイル
     */
    private String getYAMLFile() {
        IEditorInput input = fEditor.getEditorInput();
        String yamlFileName = null;
        if (input != null) {
            IFile yamlFile = ((IFileEditorInput) input).getFile();
            String tmp = yamlFile.getFullPath().toString();
            int st = tmp.indexOf('/', 1);
            yamlFileName = tmp.substring(st);
        }
        return yamlFileName;
    }

    /**
     * スキーマフォルダーのパスをラベルに設定する.
     * 
     */
    private void setSchemaFolder() {
        String schemaFolderName = fConfig.get(IConfiguration.KEY_SCHEMAFOLDER);
        String text = null;
        RGB backRGB = null;
        if (schemaFolderName != null) {
            text = schemaFolderName;
            backRGB = SCHEMA_LABEL_NORM_COLOR;
        } else {
            text = Messages.getString("YAMLEditorPart.SelectSchemaFolderMessage");
            backRGB = SCHEMA_LABEL_ERR_COLOR;
        }
        fSchemaFolderLabel.setText(text);
        fSchemaFolderLabel.setForeground(YAMLColorManager.getColorManager().getColor(SCHEMA_LABEL_FORE_COLOR));
        fSchemaFolderLabel.setBackground(YAMLColorManager.getColorManager().getColor(backRGB));
        fSchemaFolderLabel.redraw();
    }

    /**
     * YAML スキーマファイル一覧を取得して、コンボボックスにセットする.
     *
     */
    private void setSchemaFileList() {
        String schemaFolderName = fConfig.get(IConfiguration.KEY_SCHEMAFOLDER);
        String schemaFileForYAML = fConfig.get(IConfiguration.KEY_PREFIX_SCHEMAFILE + getYAMLFile());
        if (schemaFolderName != null) {
            fSchemaCombo.setEnabled(true);
            fSchemaCombo.removeAll();
            fSchemaCombo.add(Messages.getString("YAMLEditorPart.SelectSchemaMessage"));
            fSchemaCombo.select(0);
            try {
                IFolder folder = getYAMLProject().getFolder(schemaFolderName);
                IResource[] resources = folder.members();
                for (int i = 0; i < resources.length; i++) {
                    if (resources[i] instanceof IFile) {
                        IFile file = (IFile) resources[i];
                        String extension = file.getFileExtension();
                        if (extension.equalsIgnoreCase("yaml")) {
                            String schemaFileName = file.getName();
                            fSchemaCombo.add(schemaFileName);
                            if (schemaFileForYAML != null) {
                                if (schemaFileName.equals(schemaFileForYAML)) {
                                    fSchemaCombo.select(i + 1);
                                }
                            }
                        }
                    }
                }
                fSchemaCombo.redraw();
            } catch (CoreException e) {
                YAMLEditorPlugin.showDialog(e.getStatus());
            }
        } else {
            fSchemaCombo.setEnabled(false);
        }
    }

    /**
     * コンボボックスで選択された YAML スキーマファイルを返す.
     * 
     * @return YAML スキーマファイル
     */
    private String getSelectionSchemaFileName() {
        String schemaFileName = null;
        if (fSchemaCombo.getSelectionIndex() > 0) {
            schemaFileName = fSchemaCombo.getItem(fSchemaCombo.getSelectionIndex());
        }
        return schemaFileName;
    }

    /**
     * YAML ファイル－スキーマファイルの対応を保存する.
     * 
     */
    private void saveYAMLFile() {
        if (fConfig == null) {
            return;
        }
        String schemaFolderName = fConfig.get(IConfiguration.KEY_SCHEMAFOLDER);
        String schemaFileName = getSelectionSchemaFileName();
        if (schemaFileName != null) {
            fEditor.setSchemaFileName(schemaFolderName + "/" + schemaFileName);
        } else {
            fEditor.setSchemaFileName(null);
        }
        fConfig.set(IConfiguration.KEY_PREFIX_SCHEMAFILE + getYAMLFile(), schemaFileName);
        fConfig.store();
    }
}
