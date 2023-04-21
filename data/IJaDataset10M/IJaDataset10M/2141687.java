package moller.javapeg.program.categories;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import moller.javapeg.program.C;
import moller.javapeg.program.contexts.ApplicationContext;
import moller.javapeg.program.language.Language;
import moller.javapeg.program.logger.Logger;
import moller.javapeg.program.model.ModelInstanceLibrary;
import moller.util.io.StreamUtil;
import moller.util.xml.XMLAttribute;
import moller.util.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CategoryUtil {

    /**
	 * This method checks whether a category with a specified name already
	 * exists. The scope for "exists" is in the same sub category or in the top
	 * level. In other words the same category name may exist several times but
	 * not in the same scope.
	 *
	 * @param root is the content to search for potential matches of an
	 *                 already existing category.
	 *
	 * @param selectedPath if a sub category is selected, then that is
	 *        specified by this parameter, otherwise it is null and that
	 *        indicates that the top (root) level shall be examined.
	 *
	 * @param categoryName is the name of the category to search for.
	 *
	 * @return a boolean value indicating whether the category name to search
	 *         for exists in the specified scope or not. True is returned if
	 *         the specified category name in the parameter categoryName is
	 *         found in the scope specified by the parameter selectedPath.
	 */
    public static boolean alreadyExists(DefaultMutableTreeNode root, TreePath selectedPath, String categoryName) {
        if (selectedPath == null || selectedPath.getPathCount() == 1) {
            return existsInNode(categoryName, root);
        } else {
            return existsInNode(categoryName, (DefaultMutableTreeNode) selectedPath.getLastPathComponent());
        }
    }

    private static boolean existsInNode(String categoryName, DefaultMutableTreeNode node) {
        int numberOfChildren = node.getChildCount();
        if (numberOfChildren > 0) {
            for (int i = 0; i < numberOfChildren; i++) {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node.getChildAt(i);
                if (categoryName.equals(((CategoryUserObject) dmtn.getUserObject()).getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Checks whether the name of a category is valid.
	 *
	 * @param category contains the string with the category name.
	 * @return true if the string contained in the parameter category does not
	 *         start with a white space and the length of the category name is
	 *         longer than zero characters, otherwise false is returned.
	 */
    public static boolean isValid(String category) {
        return category.length() > 0 && !category.startsWith(" ");
    }

    /**
	 * @param categoriesFile
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws CategoryContentException
	 */
    private static Document parse(File categoriesFile) throws ParserConfigurationException, SAXException, IOException, CategoryContentException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document document = builder.parse(categoriesFile);
        document.normalize();
        NodeList categories = document.getElementsByTagName("categories");
        if (categories.getLength() != 1) {
            throw new CategoryContentException("Invalid xml. It is only valid to have  exactly 1 element \"categories\", But there is: " + categories.getLength() + " \"categories\" elements in the file: " + categoriesFile.getAbsolutePath());
        } else {
            Element categoriesNode = (Element) categories.item(0);
            int id = Integer.parseInt(categoriesNode.getAttribute("highest-used-id"));
            ApplicationContext.getInstance().setHighestUsedCategoryID(id);
        }
        return document;
    }

    public static void populateTreeModel(Document document, DefaultMutableTreeNode root) throws CategoryContentException {
        NodeList categoriesElementAsNodeList = document.getElementsByTagName("categories");
        if (categoriesElementAsNodeList.getLength() != 1) {
            throw new CategoryContentException("Invalid xml. It is only valid to have  exactly 1 element \"categories\", But there is: " + categoriesElementAsNodeList.getLength() + " \"categories\" elements int the loaded XML document");
        } else {
            Node categoriesElement = categoriesElementAsNodeList.item(0);
            NodeList categoryElements = categoriesElement.getChildNodes();
            if (categoryElements.getLength() > 0) {
                for (int i = 0; i < categoryElements.getLength(); i++) {
                    Element category = (Element) categoryElements.item(i);
                    populateNodeBranch(root, category);
                }
            }
        }
    }

    private static void populateNodeBranch(DefaultMutableTreeNode node, Element category) {
        String displayString = category.getAttribute("name");
        String identityString = category.getAttribute("id");
        CategoryUserObject cuo = new CategoryUserObject(displayString, identityString);
        DefaultMutableTreeNode mtn = new DefaultMutableTreeNode(cuo);
        node.add(mtn);
        if (category.hasChildNodes()) {
            NodeList categoryChildren = category.getChildNodes();
            if (categoryChildren.getLength() > 0) {
                for (int i = 0; i < categoryChildren.getLength(); i++) {
                    Element child = (Element) categoryChildren.item(i);
                    populateNodeBranch(mtn, child);
                }
            }
        }
    }

    /**
	 * @param categoriesFile
	 * @param document
	 */
    @SuppressWarnings("unchecked")
    public static void store(File categoriesFile, DefaultMutableTreeNode root) {
        OutputStream os = null;
        boolean displayErrorMessage = false;
        try {
            String encoding = "UTF-8";
            os = new FileOutputStream(categoriesFile);
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter w = factory.createXMLStreamWriter(os, encoding);
            XMLUtil.writeStartDocument(encoding, "1.0", w);
            XMLUtil.writeComment("This XML file contains the available categories and the logical structure" + C.LS + "of the categories. The content of this file is used and modified by the" + C.LS + "application JavaPEG and should not be edited manually, since any change might be" + C.LS + "overwritten by the JavaPEG application or corrupt the file if the change is invalid" + C.LS, w);
            XMLUtil.writeElementStart("categories", "highest-used-id", Integer.toString(ApplicationContext.getInstance().getHighestUsedCategoryID()), w);
            Enumeration<DefaultMutableTreeNode> children = root.children();
            while (children.hasMoreElements()) {
                storeChild(children.nextElement(), w);
            }
            XMLUtil.writeElementEnd(w);
            w.flush();
        } catch (XMLStreamException xse) {
            Logger logger = Logger.getInstance();
            logger.logFATAL("Could not store content of categories file (" + categoriesFile.getAbsolutePath() + "). See stacktrace below for details");
            logger.logFATAL(xse);
            displayErrorMessage = true;
        } catch (FileNotFoundException fnex) {
            Logger logger = Logger.getInstance();
            logger.logFATAL("Could not store content of categories file (" + categoriesFile.getAbsolutePath() + "). See stacktrace below for details");
            logger.logFATAL(fnex);
            displayErrorMessage = true;
        } finally {
            StreamUtil.close(os, true);
            if (displayErrorMessage) {
                Language lang = Language.getInstance();
                displayErrorMessage(lang.get("category.categoriesModel.store.error"), lang.get("errormessage.maingui.errorMessageLabel"));
                System.exit(1);
            }
        }
    }

    /**
	 * @param child
	 * @param w
	 * @throws XMLStreamException
	 */
    @SuppressWarnings("unchecked")
    private static void storeChild(DefaultMutableTreeNode node, XMLStreamWriter w) throws XMLStreamException {
        XMLAttribute[] xmlAttributes = new XMLAttribute[2];
        if (node.getChildCount() > 0) {
            CategoryUserObject cuo = (CategoryUserObject) node.getUserObject();
            xmlAttributes[0] = new XMLAttribute("id", cuo.getIdentity());
            xmlAttributes[1] = new XMLAttribute("name", cuo.getName());
            XMLUtil.writeElementStart("category", xmlAttributes, w);
            Enumeration<DefaultMutableTreeNode> children = node.children();
            while (children.hasMoreElements()) {
                storeChild(children.nextElement(), w);
            }
            XMLUtil.writeElementEnd(w);
        } else {
            CategoryUserObject cuo = (CategoryUserObject) node.getUserObject();
            xmlAttributes[0] = new XMLAttribute("id", cuo.getIdentity());
            xmlAttributes[1] = new XMLAttribute("name", cuo.getName());
            XMLUtil.writeElement("category", null, xmlAttributes, w);
        }
    }

    public static JTree createCategoriesTree() {
        JTree categoriesTree = new JTree();
        categoriesTree.setModel(ModelInstanceLibrary.getInstance().getCategoriesModel());
        categoriesTree.setShowsRootHandles(true);
        categoriesTree.setRootVisible(false);
        return categoriesTree;
    }

    public static TreeNode createCategoriesModel() {
        File categoriesFile = new File(C.USER_HOME + C.FS + "javapeg-" + C.JAVAPEG_VERSION + C.FS + "config" + C.FS + "categories.xml");
        Document document = null;
        DefaultMutableTreeNode root = null;
        boolean createCategoriesModelSuccess = false;
        Logger logger = Logger.getInstance();
        try {
            document = CategoryUtil.parse(categoriesFile);
            root = new DefaultMutableTreeNode(new CategoryUserObject("root", "-1"));
            CategoryUtil.populateTreeModel(document, root);
            createCategoriesModelSuccess = true;
        } catch (ParserConfigurationException pce) {
            logger.logFATAL("Could not parse the categories file. See stacktrace below for details.");
            logger.logFATAL(pce);
        } catch (SAXException se) {
            logger.logFATAL("Could not parse the categories file. See stacktrace below for details.");
            logger.logFATAL(se);
        } catch (IOException iox) {
            logger.logFATAL("Could not parse the categories file. See stacktrace below for details.");
            logger.logFATAL(iox);
        } catch (CategoryContentException cce) {
            logger.logFATAL("Invalid content in categories file. See stacktrace below for details.");
            logger.logFATAL(cce);
        }
        if (!createCategoriesModelSuccess) {
            Language lang = Language.getInstance();
            displayErrorMessage(lang.get("category.categoriesModel.create.error"), lang.get("errormessage.maingui.errorMessageLabel"));
            System.exit(1);
        }
        return root;
    }

    private static void displayErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
