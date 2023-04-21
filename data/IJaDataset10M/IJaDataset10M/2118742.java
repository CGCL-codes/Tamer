package eu.toennies.abc.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Vector;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.Task.BlockingScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.LookUtils;
import eu.toennies.abc.AudioBookCreater;
import eu.toennies.abc.conversion.FinishListener;
import eu.toennies.abc.gui.StatusBar.StatusType;
import eu.toennies.abc.model.CD;
import eu.toennies.abc.model.Chapter;
import eu.toennies.abc.tasks.AboutTask;
import eu.toennies.abc.tasks.AddChapterTask;
import eu.toennies.abc.tasks.CddbTask;
import eu.toennies.abc.tasks.ChooseCoverTask;
import eu.toennies.abc.tasks.ConversionTask;
import eu.toennies.abc.tasks.MergeTask;
import eu.toennies.abc.tasks.MoveRowTask;
import eu.toennies.abc.tasks.Mp3OpenTask;
import eu.toennies.abc.tasks.Mp4OpenTask;
import eu.toennies.abc.tasks.RemoveChapterTask;
import eu.toennies.abc.tasks.MoveRowTask.Mode;
import eu.toennies.abc.util.Factory;

/**
 * 
 * @author $LastChangedBy: sushi_78 $
 * @version $LastChangedRevision: 12 $ $LastChangedDate: 2008-03-06 18:26:51
 *          +0100 (Do, 06 Mrz 2008) $
 */
public class AbcView extends FrameView implements FinishListener {

    private static final Logger log = LoggerFactory.getLogger(AbcView.class);

    private static final String MP4_FILE_SELECTED = "mp4FileSelected";

    private static final String MP3_FILES_SELECTED = "mp3FilesSelected";

    private StatusBar statusBar;

    private String[] toolbarActionNames = { "openMp3", "mergeFiles", "---", "cddbCall", "---", "createAudiobook" };

    private String[] fileMenuActionNames = { "openMp3", "mergeFiles", "---", "createAudiobook", "---", "quit" };

    private String[] cdMenuActionNames = { "cddbCall" };

    private String[] helpMenuActionNames = { "about" };

    private JTextArea mp3FilesField;

    private JTextField mp4FileField;

    private JTextField titleField;

    private JTextField genreField;

    private JTextField artistField;

    private JLabel coverLabel;

    private File audiobookFile;

    private File audiobookCover;

    private JTable chapterTable;

    private DefaultTableModel chapterModel;

    private JButton addCh;

    private JButton remCh;

    private JButton upCh;

    private JButton downCh;

    private JButton topCh;

    private JButton bottomCh;

    public AbcView() {
        super(AudioBookCreater.getInstance());
        initComponents();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        menuBar.add(createMenu("cdMenu", cdMenuActionNames));
        menuBar.add(createMenu("helpMenu", helpMenuActionNames));
        this.setMenuBar(menuBar);
        this.setStatusBar(createStatusBar());
        this.setToolBar(createToolBar());
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(buildSplit());
        this.setComponent(panel);
    }

    private JComponent buildSplit() {
        JSplitPane verticalSplit = Factory.createStrippedSplitPane(JSplitPane.VERTICAL_SPLIT, buildFileForm(), buildInformationForm(), 0.2f);
        verticalSplit.setOpaque(false);
        return verticalSplit;
    }

    private JComponent createStatusBar() {
        statusBar = new StatusBar(AudioBookCreater.getInstance(), getContext().getTaskMonitor());
        return statusBar;
    }

    private JComponent buildFileForm() {
        FormLayout layout = new FormLayout("pref, 3dlu, pref:grow, 3dlu, 80px", "pref, 5px, pref");
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();
        JButton openButton = new JButton();
        openButton = new JButton("Choose a File...");
        openButton.setAction(getAction("openMp4"));
        openButton.setHideActionText(true);
        panel.add(new JLabel("File"), cc.xy(1, 1));
        panel.add(mp4FileField, cc.xy(3, 1));
        panel.add(openButton, cc.xy(5, 1));
        return Factory.createInternalFrame(AudioBookCreater.getResourceMap().getString("mp4File"), panel);
    }

    /**
	 * Builds the panel. Initializes and configures components first, then
	 * creates a FormLayout, configures the layout, creates a builder, sets a
	 * border, and finally adds the components.
	 * 
	 * @return the built panel
	 */
    private JComponent buildInformationForm() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow, 6dlu, center:80px", "pref, 5px, " + "pref, 5px, " + "pref, 5px, " + "center:pref, 5px, pref");
        JButton coverButton = new JButton("Choose Cover");
        coverButton.setAction(getAction("openCover"));
        ButtonBarBuilder cBuilder = new ButtonBarBuilder();
        cBuilder = new ButtonBarBuilder();
        cBuilder.addGlue();
        cBuilder.addGriddedButtons(new JButton[] { coverButton });
        chapterModel = new DefaultTableModel(Chapter.COLUMNS, 0);
        chapterTable = new JTable(chapterModel);
        chapterTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        int tableFontSize = chapterTable.getFont().getSize();
        int minimumRowHeight = tableFontSize + 6;
        int defaultRowHeight = LookUtils.IS_LOW_RESOLUTION ? 17 : 18;
        chapterTable.setRowHeight(Math.max(minimumRowHeight, defaultRowHeight));
        chapterTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        Component tablePane = new JScrollPane(chapterTable);
        ButtonStackBuilder bBuilder = new ButtonStackBuilder();
        bBuilder.addGridded(addCh);
        bBuilder.addRelatedGap();
        bBuilder.addGridded(topCh);
        bBuilder.addRelatedGap();
        bBuilder.addGridded(upCh);
        bBuilder.addRelatedGap();
        bBuilder.addGridded(downCh);
        bBuilder.addRelatedGap();
        bBuilder.addGridded(bottomCh);
        bBuilder.addRelatedGap();
        bBuilder.addGridded(remCh);
        JPanel span = new JPanel(layout);
        span.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();
        span.add(new JLabel("Title"), cc.xy(1, 1));
        span.add(titleField, cc.xy(3, 1));
        span.add(new JLabel("Artist"), cc.xy(1, 3));
        span.add(artistField, cc.xy(3, 3));
        span.add(new JLabel("Genre"), cc.xy(1, 5));
        span.add(genreField, cc.xy(3, 5));
        span.add(coverLabel, cc.xywh(5, 1, 1, 5));
        span.add(cBuilder.getPanel(), cc.xy(3, 7));
        span.add(tablePane, cc.xy(3, 9));
        span.add(bBuilder.getPanel(), cc.xy(5, 9));
        return Factory.createInternalFrame(AudioBookCreater.getResourceMap().getString("metadata"), span);
    }

    /**
	 * Creates, initializes and configures the UI components. Real applications
	 * may further bind the components to underlying models.
	 */
    private void initComponents() {
        mp3FilesField = new JTextArea();
        mp3FilesField.setRows(10);
        mp4FileField = new JTextField();
        titleField = new JTextField();
        artistField = new JTextField(6);
        genreField = new JTextField(10);
        genreField.setText("Spoken Books");
        coverLabel = new JLabel();
        addCh = new JButton();
        addCh.setAction(getAction("addCh"));
        remCh = new JButton();
        remCh.setAction(getAction("remCh"));
        upCh = new JButton();
        upCh.setAction(getAction("up"));
        downCh = new JButton();
        downCh.setAction(getAction("down"));
        topCh = new JButton();
        topCh.setAction(getAction("top"));
        bottomCh = new JButton();
        bottomCh.setAction(getAction("bottom"));
    }

    private final JMenu createMenu(String menuName, String[] actionNames) {
        JMenu menu = new JMenu();
        menu.setName(menuName);
        for (String actionName : actionNames) {
            if (actionName.equals("---")) {
                menu.add(new JSeparator());
            } else {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setAction(getAction(actionName));
                menu.add(menuItem);
            }
        }
        return menu;
    }

    private final javax.swing.Action getAction(String actionName) {
        ActionMap actionMap = this.getContext().getActionMap(this.getClass(), this);
        return actionMap.get(actionName);
    }

    private final JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        Border border = new EmptyBorder(2, 9, 2, 9);
        for (String actionName : toolbarActionNames) {
            JButton button = new JButton();
            button.setBorder(border);
            button.setVerticalTextPosition(JButton.BOTTOM);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setAction(getAction(actionName));
            button.setFocusable(false);
            toolBar.add(button);
        }
        return toolBar;
    }

    public void setStatusMessage(String msg, StatusType type) {
        statusBar.setMessage(msg, type);
    }

    public void setCD(CD cd) {
        setStatusMessage("using CDDB data", StatusType.INFO);
        artistField.setText(cd.getArtist());
        titleField.setText(cd.getTitle());
        genreField.setText(cd.getGenre());
        for (Chapter chapter : cd.getChapters()) {
            Vector<String> data = new Vector<String>();
            data.add(String.valueOf(chapter.getNumber()));
            data.add(chapter.getFile());
            data.add(chapter.getTitle());
            data.add(chapter.getStartTime());
            data.add(chapter.getImage());
            chapterModel.addRow(data);
        }
    }

    public DefaultTableModel getChapterModel() {
        return chapterModel;
    }

    public void setCoverFile(File absoluteFile) {
        audiobookCover = absoluteFile;
        Icon icon = new ImageIcon(getScaledImage(absoluteFile, 64, 64));
        coverLabel.setIcon(icon);
        coverLabel.setSize(16, 16);
        coverLabel.updateUI();
    }

    private Image getScaledImage(File srcFile, int w, int h) {
        ImageIcon srcImg = new ImageIcon(srcFile.getPath());
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg.getImage(), 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public File getAudiobookFile() {
        if (audiobookFile == null) {
            if (mp3FilesField.getText() != null) {
                audiobookFile = new File(mp3FilesField.getText());
            }
        }
        return audiobookFile;
    }

    public boolean isMp4FileSelected() {
        if (mp4FileField.getText().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMp3FilesSelected() {
        if (mp3FilesField.getText().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void addAudiobookFile(File select) {
        mp3FilesField.append(select.getAbsolutePath() + "\n");
        firePropertyChange(MP3_FILES_SELECTED, false, true);
    }

    public void setAudiobookFile(File select) {
        audiobookFile = select;
        mp4FileField.setText(select.getAbsolutePath());
        firePropertyChange(MP4_FILE_SELECTED, false, true);
    }

    public String getAudiobookTitle() {
        return titleField.getText();
    }

    public String getAudiobookArtist() {
        return artistField.getText();
    }

    public String getAudiobookGenre() {
        return genreField.getText();
    }

    public File getAudiobookCover() {
        return audiobookCover;
    }

    public void finishedWithError(final String errorMessage) {
        statusBar.stopBusyAnimation();
        JOptionPane.showMessageDialog(this.getFrame(), errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void finishedWithoutError() {
        statusBar.stopBusyAnimation();
        JOptionPane.showMessageDialog(this.getFrame(), "Additional info", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void canceled() {
        statusBar.stopBusyAnimation();
    }

    @Action(block = BlockingScope.ACTION)
    public Task<List<CD>, Void> cddbCall() {
        Task<List<CD>, Void> task = new CddbTask();
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<Void, Void> addCh() {
        Task<Void, Void> task = new AddChapterTask(chapterModel);
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<Void, Void> remCh() {
        Task<Void, Void> task = new RemoveChapterTask(chapterModel);
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<Void, Void> up() {
        Task<Void, Void> task = new MoveRowTask(chapterModel, chapterTable, Mode.UP);
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<Void, Void> down() {
        Task<Void, Void> task = new MoveRowTask(chapterModel, chapterTable, Mode.DOWN);
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<Void, Void> top() {
        Task<Void, Void> task = new MoveRowTask(chapterModel, chapterTable, Mode.TOP);
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<Void, Void> bottom() {
        Task<Void, Void> task = new MoveRowTask(chapterModel, chapterTable, Mode.BOTTOM);
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<File, Void> openCover() {
        Task<File, Void> task = new ChooseCoverTask();
        return task;
    }

    @Action(block = BlockingScope.APPLICATION, enabledProperty = MP4_FILE_SELECTED)
    public Task<File, Void> createAudiobook() {
        Task<File, Void> task = new ConversionTask(titleField.getText(), artistField.getText(), genreField.getText(), audiobookCover, chapterModel);
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<File[], Chapter> openMp3() {
        Task<File[], Chapter> task = new Mp3OpenTask(chapterModel);
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<File, Void> openMp4() {
        Task<File, Void> task = new Mp4OpenTask();
        return task;
    }

    @Action(block = BlockingScope.APPLICATION, enabledProperty = MP3_FILES_SELECTED)
    public Task<Void, Void> mergeFiles() {
        Task<Void, Void> task = new MergeTask();
        return task;
    }

    @Action(block = BlockingScope.ACTION)
    public Task<Void, Void> about() {
        Task<Void, Void> task = new AboutTask();
        return task;
    }

    public File getM4File() {
        if (mp4FileField.getText() != null) {
            return new File(mp4FileField.getText());
        } else {
            return null;
        }
    }

    public String[] getMp3Files() {
        String text = mp3FilesField.getText();
        String[] files = text.split("\n");
        return files;
    }

    public void setArtist(String artist) {
        artistField.setText(artist);
    }

    public void setTitle(String title) {
        titleField.setText(title);
    }

    public String getTitle() {
        return titleField.getText();
    }

    public void setGenre(String genre) {
        genreField.setText(genre);
    }
}
