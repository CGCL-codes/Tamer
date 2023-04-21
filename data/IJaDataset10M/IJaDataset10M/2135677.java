package br.cefetpe.tsi.ww;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.ImageComponent;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.Listbox;
import com.sun.webui.jsf.component.MessageGroup;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Tab;
import com.sun.webui.jsf.component.TabSet;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.Tree;
import com.sun.webui.jsf.component.TreeNode;
import com.sun.webui.jsf.model.DefaultOptionsList;
import com.sun.webui.jsf.model.Option;
import java.io.File;
import java.util.ArrayList;
import javax.faces.FacesException;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.event.ValueChangeEvent;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Cobweb;
import weka.clusterers.SimpleKMeans;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 */
public class modelling extends AbstractPageBean {

    private int __placeholder;

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Page page1 = new Page();

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page p) {
        this.page1 = p;
    }

    private Html html1 = new Html();

    public Html getHtml1() {
        return html1;
    }

    public void setHtml1(Html h) {
        this.html1 = h;
    }

    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }

    private Link link1 = new Link();

    public Link getLink1() {
        return link1;
    }

    public void setLink1(Link l) {
        this.link1 = l;
    }

    private Body body1 = new Body();

    public Body getBody1() {
        return body1;
    }

    public void setBody1(Body b) {
        this.body1 = b;
    }

    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(Form f) {
        this.form1 = f;
    }

    private ImageComponent image1 = new ImageComponent();

    public ImageComponent getImage1() {
        return image1;
    }

    public void setImage1(ImageComponent ic) {
        this.image1 = ic;
    }

    private ImageComponent image2 = new ImageComponent();

    public ImageComponent getImage2() {
        return image2;
    }

    public void setImage2(ImageComponent ic) {
        this.image2 = ic;
    }

    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(StaticText st) {
        this.staticText1 = st;
    }

    private StaticText staticText2 = new StaticText();

    public StaticText getStaticText2() {
        return staticText2;
    }

    public void setStaticText2(StaticText st) {
        this.staticText2 = st;
    }

    private Tree tree1 = new Tree();

    public Tree getTree1() {
        return tree1;
    }

    public void setTree1(Tree t) {
        this.tree1 = t;
    }

    private TreeNode treeNode1 = new TreeNode();

    public TreeNode getTreeNode1() {
        return treeNode1;
    }

    public void setTreeNode1(TreeNode tn) {
        this.treeNode1 = tn;
    }

    private ImageComponent image3 = new ImageComponent();

    public ImageComponent getImage3() {
        return image3;
    }

    public void setImage3(ImageComponent ic) {
        this.image3 = ic;
    }

    private TreeNode treeNode2 = new TreeNode();

    public TreeNode getTreeNode2() {
        return treeNode2;
    }

    public void setTreeNode2(TreeNode tn) {
        this.treeNode2 = tn;
    }

    private ImageComponent image4 = new ImageComponent();

    public ImageComponent getImage4() {
        return image4;
    }

    public void setImage4(ImageComponent ic) {
        this.image4 = ic;
    }

    private MessageGroup messageGroup1 = new MessageGroup();

    public MessageGroup getMessageGroup1() {
        return messageGroup1;
    }

    public void setMessageGroup1(MessageGroup mg) {
        this.messageGroup1 = mg;
    }

    private TreeNode treeNode3 = new TreeNode();

    public TreeNode getTreeNode3() {
        return treeNode3;
    }

    public void setTreeNode3(TreeNode tn) {
        this.treeNode3 = tn;
    }

    private ImageComponent image5 = new ImageComponent();

    public ImageComponent getImage5() {
        return image5;
    }

    public void setImage5(ImageComponent ic) {
        this.image5 = ic;
    }

    private TreeNode treeNode4 = new TreeNode();

    public TreeNode getTreeNode4() {
        return treeNode4;
    }

    public void setTreeNode4(TreeNode tn) {
        this.treeNode4 = tn;
    }

    private ImageComponent image6 = new ImageComponent();

    public ImageComponent getImage6() {
        return image6;
    }

    public void setImage6(ImageComponent ic) {
        this.image6 = ic;
    }

    private TreeNode treeNode5 = new TreeNode();

    public TreeNode getTreeNode5() {
        return treeNode5;
    }

    public void setTreeNode5(TreeNode tn) {
        this.treeNode5 = tn;
    }

    private ImageComponent image7 = new ImageComponent();

    public ImageComponent getImage7() {
        return image7;
    }

    public void setImage7(ImageComponent ic) {
        this.image7 = ic;
    }

    private TabSet j48ApplyTabSet = new TabSet();

    public TabSet getJ48ApplyTabSet() {
        return j48ApplyTabSet;
    }

    public void setJ48ApplyTabSet(TabSet ts) {
        this.j48ApplyTabSet = ts;
    }

    private Tab fileListTab = new Tab();

    public Tab getFileListTab() {
        return fileListTab;
    }

    public void setFileListTab(Tab t) {
        this.fileListTab = t;
    }

    private Tab optionsTab = new Tab();

    public Tab getOptionsTab() {
        return optionsTab;
    }

    public void setOptionsTab(Tab t) {
        this.optionsTab = t;
    }

    private Tab outputsTab = new Tab();

    public Tab getOutputsTab() {
        return outputsTab;
    }

    public void setOutputsTab(Tab t) {
        this.outputsTab = t;
    }

    private HtmlPanelGrid gridPanel1 = new HtmlPanelGrid();

    public HtmlPanelGrid getGridPanel1() {
        return gridPanel1;
    }

    public void setGridPanel1(HtmlPanelGrid hpg) {
        this.gridPanel1 = hpg;
    }

    private HtmlPanelGrid gridPanel2 = new HtmlPanelGrid();

    public HtmlPanelGrid getGridPanel2() {
        return gridPanel2;
    }

    public void setGridPanel2(HtmlPanelGrid hpg) {
        this.gridPanel2 = hpg;
    }

    private HtmlPanelGrid gridPanel3 = new HtmlPanelGrid();

    public HtmlPanelGrid getGridPanel3() {
        return gridPanel3;
    }

    public void setGridPanel3(HtmlPanelGrid hpg) {
        this.gridPanel3 = hpg;
    }

    private Listbox listbox1 = new Listbox();

    public Listbox getListbox1() {
        return listbox1;
    }

    public void setListbox1(Listbox l) {
        this.listbox1 = l;
    }

    private DefaultOptionsList listbox1DefaultOptions = new DefaultOptionsList();

    public DefaultOptionsList getListbox1DefaultOptions() {
        return listbox1DefaultOptions;
    }

    public void setListbox1DefaultOptions(DefaultOptionsList dol) {
        this.listbox1DefaultOptions = dol;
    }

    private TextArea outputArea = new TextArea();

    public TextArea getOutputArea() {
        return outputArea;
    }

    public void setOutputArea(TextArea ta) {
        this.outputArea = ta;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public modelling() {
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     * 
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("modelling Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    public void prerender() {
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    public void destroy() {
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    public void radioButtonGroup1_processValueChange(ValueChangeEvent vce) {
    }

    public String treeNode2_action() {
        this.j48ApplyTabSet.setVisible(true);
        File dataDir = new File("c:/Projects/ww/web/data/");
        File[] filesAvaliable = dataDir.listFiles();
        ArrayList<Option> fileListData = new ArrayList<Option>();
        for (File file : filesAvaliable) {
            fileListData.add(new Option(file.getAbsolutePath()));
        }
        this.listbox1DefaultOptions.setOptions(fileListData.toArray(new Option[fileListData.size()]));
        return null;
    }

    public String treeNode4_action() {
        String[] options = new String[2];
        options[0] = "-t";
        options[1] = "C:/Projects/ww/web/data/contact-lenses.arff";
        Cobweb cobweb = new Cobweb();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(ClusterEvaluation.evaluateClusterer(cobweb, options) + "\n");
            sb.append("========================================\n");
            sb.append(cobweb.toString() + "\n");
            sb.append("========================================\n");
            sb.append(cobweb.getTechnicalInformation() + "\n");
            sb.append("========================================\n");
            sb.append(cobweb.graph() + "\n");
            sb.append("========================================\n");
            sb.append(cobweb.graphType() + "\n");
        } catch (Exception e) {
            error(e.getMessage());
        }
        return null;
    }

    public String treeNode5_action() {
        String[] options = new String[2];
        options[0] = "-t";
        options[1] = "C:/Projects/ww/web/data/contact-lenses.arff";
        SimpleKMeans kmeans = new SimpleKMeans();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(ClusterEvaluation.evaluateClusterer(kmeans, options) + "\n");
            sb.append("========================================\n");
            sb.append(kmeans.toString() + "\n");
            sb.append("========================================\n");
            sb.append(kmeans.globalInfo() + "\n");
            sb.append("========================================\n");
        } catch (Exception e) {
            error(e.getMessage());
        }
        return null;
    }

    public void listbox1_processValueChange(ValueChangeEvent event) {
        String[] options = new String[2];
        options[0] = "-t";
        options[1] = event.getNewValue().toString();
        J48 j48Tree = new J48();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(Evaluation.evaluateModel(j48Tree, options) + "\n");
            sb.append("========================================\n");
            sb.append(j48Tree.toString() + "\n");
            sb.append("========================================\n");
            sb.append(j48Tree.toSummaryString() + "\n");
            sb.append("========================================\n");
            sb.append(j48Tree.graph() + "\n");
            sb.append("========================================\n");
            sb.append(j48Tree.graphType() + "\n");
            this.outputArea.setText(sb.toString());
        } catch (Exception e) {
            error(e.getMessage());
        }
        this.optionsTab.setSelectedChildId(this.outputsTab.getId());
    }
}
