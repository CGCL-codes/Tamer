package vavi.apps.treeView;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import vavi.awt.Selectable;
import vavi.swing.event.EditorEvent;
import vavi.swing.event.EditorListener;
import vavi.swing.event.EditorSupport;

/**
 * The base node class for TreeView.
 * 
 * @event EditorEvent("expand")
 * @event EditorEvent("delete")
 * @event EditorEvent("insert")
 * 
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 */
public abstract class TreeViewTreeNode extends DefaultMutableTreeNode implements Selectable {

    /** ���\�[�X�o���h�� */
    protected static final ResourceBundle rb = ResourceBundle.getBundle("vavi.apps.treeView.TreeViewResource", Locale.getDefault());

    /**
     * �J�b�g���ꂽ���ǂ���
     */
    protected boolean isCut = false;

    /** */
    public boolean isCut() {
        return isCut;
    }

    /**
     * TreeView �Ŏg�p�����{�̃c���[�m�[�h���쐬���܂��D
     * 
     * @param userObject �m�[�h�̃f�[�^
     */
    public TreeViewTreeNode(Object userObject) {
        super(userObject);
        this.userObject = userObject;
    }

    /** */
    public String toString() {
        return userObject.toString();
    }

    /** */
    public void setActionStates() {
    }

    /**
     * �I�[�v�����܂��D
     * 
     * @throws TreeViewException �ł��Ȃ������ꍇ
     */
    public void open() throws TreeViewException {
        throw new TreeViewException(rb.getString("action.open.error"));
    }

    /**
     * ���O��ύX���܂��D
     * 
     * @param name �ύX��̕\����
     * @throws TreeViewException �ł��Ȃ������ꍇ
     */
    public void rename(String name) throws TreeViewException {
        throw new TreeViewException(rb.getString("action.rename.error"));
    }

    /**
     * �J�b�g���܂��D
     * 
     * @throws TreeViewException �ł��Ȃ������ꍇ
     */
    public void cut() throws TreeViewException {
        throw new TreeViewException(rb.getString("action.cut.error"));
    }

    /**
     * �R�s�[���܂��D
     * 
     * @throws TreeViewException �R�s�[�ł��Ȃ�����
     */
    public void copy() throws TreeViewException {
        throw new TreeViewException(rb.getString("action.copy.error"));
    }

    /**
     * �y�[�X�g���܂��D
     * 
     * @throws TreeViewException �ł��Ȃ������ꍇ
     */
    public void paste(TreeViewTreeNode from) throws TreeViewException {
        throw new TreeViewException(rb.getString("action.paste.error"));
    }

    /**
     * �ړ����܂��D
     * 
     * @throws TreeViewException �ł��Ȃ������ꍇ
     */
    protected void move(TreeViewTreeNode to) throws TreeViewException {
        throw new TreeViewException(rb.getString("action.paste.error"));
    }

    /**
     * �폜���܂��D
     * 
     * @throws TreeViewException �ł��Ȃ������ꍇ
     */
    public void delete() throws TreeViewException {
        throw new TreeViewException(rb.getString("action.delete.error"));
    }

    /** �I�[�v�������Ƃ��r���[��ύX���܂��D */
    protected void openController() {
        fireEditorUpdated(new EditorEvent(this, "expand", new TreePath(getPath())));
    }

    /** �폜�����Ƃ��r���[��ύX���܂��D */
    protected void deleteController() {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) getParent();
        int index = parent.getIndex(this);
        parent.remove(index);
        this.removeFromParent();
        int[] indices = new int[1];
        indices[0] = index;
        DefaultMutableTreeNode[] removed = new DefaultMutableTreeNode[1];
        removed[0] = this;
        fireEditorUpdated(new EditorEvent(this, "delete", new Object[] { parent, indices, removed }));
    }

    /**
     * �y�[�X�g�����Ƃ��r���[��ύX���܂��D �J�b�g���R�s�[�ɂ��킹�� from �����̂��̂� �R�s�[�������̂������[�U���ݒ肷��K�v������܂��D
     */
    protected void pasteController(TreeViewTreeNode from) {
        addController(from);
    }

    /** �ړ������Ƃ��r���[��ύX���܂��D */
    protected void moveController(TreeViewTreeNode to) {
        deleteController();
        to.addController(this);
    }

    /** �ǉ������Ƃ��r���[��ύX���܂��D */
    protected void addController(DefaultMutableTreeNode toAdd) {
        this.add(toAdd);
        fireEditorUpdated(new EditorEvent(this, "insert", this));
        openController();
    }

    /** �I�[�v���ł��邩�ǂ��� */
    public boolean canOpen() {
        return false;
    }

    /** ���l�[���ł��邩�ǂ��� */
    public boolean canRename() {
        return false;
    }

    /** �J�b�g�ł��邩�ǂ��� */
    public boolean canCut() {
        return false;
    }

    /** �R�s�[�ł��邩�ǂ��� */
    public boolean canCopy() {
        return false;
    }

    /** �y�[�X�g�ł��邩�ǂ��� */
    public boolean canPaste(TreeViewTreeNode from) {
        return false;
    }

    /** �폜�ł��邩�ǂ��� */
    public boolean canDelete() {
        return false;
    }

    /**
     * {@link DefaultMutableTreeNode#getUserObject()} �� transient �ł��B
     * ���̃c���[�m�[�h�𒼗񉻂��邽�߂ɃI�[�o���C�h���܂��B
     */
    protected Object userObject;

    /**
     * {@link DefaultMutableTreeNode#getUserObject()} �� transient �ł��B
     * ���̃c���[�m�[�h�𒼗񉻂��邽�߂ɃI�[�o���C�h���܂��B
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    /**
     * {@link DefaultMutableTreeNode#getUserObject()} �� transient �ł��B
     * ���̃c���[�m�[�h�𒼗񉻂��邽�߂ɃI�[�o���C�h���܂��B
     */
    public Object getUserObject() {
        return userObject;
    }

    /** EditorEvent �@�\�̃��[�e�B���e�B */
    private static EditorSupport editorSupport = new EditorSupport();

    /** Editor ���X�i�[��ǉ����܂��D */
    public synchronized void addEditorListener(EditorListener l) {
        editorSupport.addEditorListener(l);
    }

    /** Editor ���X�i�[���폜���܂��D */
    public synchronized void removeEditorListener(EditorListener l) {
        editorSupport.removeEditorListener(l);
    }

    /** EditorEvent �𔭍s���܂��D */
    protected void fireEditorUpdated(EditorEvent ev) {
        editorSupport.fireEditorUpdated(ev);
    }

    private boolean isSelected;

    /** */
    public boolean isSelected() {
        return isSelected;
    }

    /** */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
