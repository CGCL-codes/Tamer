package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import jp.ac.osaka_u.ist.sdl.scorpio.Entity;
import jp.ac.osaka_u.ist.sdl.scorpio.ScorpioGUI;

/**
 * GUI�ŃR�[�h�N���[����\���N���X
 * 
 * @author higo
 * 
 */
public class CodeCloneInfo implements Entity, Comparable<CodeCloneInfo> {

    /**
	 * �R���X�g���N�^
	 */
    public CodeCloneInfo() {
        this.elements = new TreeSet<ElementInfo>();
        this.calls = new TreeSet<MethodCallInfo>();
    }

    /**
	 * �v�f��������
	 * 
	 * @param element
	 *            ������v�f
	 */
    public void add(final ElementInfo element) {
        if (null == element) {
            throw new IllegalArgumentException();
        }
        this.elements.add(element);
    }

    /**
	 * �R�[�h�N���[�����Ȃ��v�f��SortedSet��Ԃ�
	 * 
	 * @return�@�R�[�h�N���[�����Ȃ��v�f��SortedSet
	 */
    public SortedSet<ElementInfo> getElements() {
        return Collections.unmodifiableSortedSet(this.elements);
    }

    /**
	 * ��Ŏw�肳�ꂽ���\�b�h�Ɋ܂܂��v�f��SortedSet��Ԃ�
	 * 
	 * @param methodID
	 * @return
	 */
    public SortedSet<ElementInfo> getElements(final int methodID) {
        final SortedSet<ElementInfo> elements = new TreeSet<ElementInfo>();
        for (final ElementInfo element : this.getElements()) {
            if (element.getMethodID() == methodID) {
                elements.add(element);
            }
        }
        return elements;
    }

    public void add(final MethodCallInfo call) {
        this.calls.add(call);
    }

    public SortedSet<MethodCallInfo> getCalls() {
        return new TreeSet<MethodCallInfo>(this.calls);
    }

    /**
	 * �R�[�h�N���[���̑傫����Ԃ�
	 * 
	 * @return�@�R�[�h�N���[���̑傫��
	 */
    public int getLength() {
        return this.elements.size();
    }

    /**
	 * �R�[�h�N���[�����Ȃ��擪�̗v�f��Ԃ�
	 * 
	 * @return �R�[�h�N���[�����Ȃ��擪�̗v�f
	 */
    public ElementInfo getFirstElement() {
        return this.elements.first();
    }

    /**
	 * �R�[�h�N���[�����Ȃ��Ō�̗v�f��Ԃ�
	 * 
	 * @return �R�[�h�N���[�����Ȃ��Ō�̗v�f
	 */
    public ElementInfo getLastElement() {
        return this.elements.last();
    }

    public void setID(final int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    /**
	 * �M���b�v�̐���ݒ肷��
	 * 
	 * @param gap
	 *            �M���b�v�̐�
	 */
    public void setNumberOfGapps(final int gap) {
        this.gap = gap;
    }

    /**
	 * �M���b�v�̐���Ԃ�
	 * 
	 * @return�@�M���b�v�̐�
	 */
    public int getNumberOfGapps() {
        return this.gap;
    }

    /**
	 * ���̃R�[�h�N���[���̗v�f���܂ރ��\�b�h�̐���ݒ肷��
	 * 
	 * @param method
	 */
    public void setNumberOfMethods(final int method) {
        this.method = method;
    }

    /**
	 * ���̃R�[�h�N���[���̗v�f���܂ރ��\�b�h�̐���Ԃ�
	 * 
	 * @param method
	 */
    public int getNumberOfMethods() {
        return this.method;
    }

    /**
	 * ���̃N���[���Ɋ܂܂��v�f�����L���郁�\�b�h�̏W����Ԃ�
	 * 
	 * @return
	 */
    public SortedSet<MethodInfo> getOwnerMethods() {
        final SortedSet<MethodInfo> ownerMethods = new TreeSet<MethodInfo>();
        for (final ElementInfo element : this.elements) {
            final int methodID = element.getMethodID();
            final MethodInfo ownerMethod = MethodController.getInstance(ScorpioGUI.ID).getMethod(methodID);
            ownerMethods.add(ownerMethod);
        }
        return ownerMethods;
    }

    /**
	 * ��r�֐�
	 */
    @Override
    public int compareTo(CodeCloneInfo o) {
        final Iterator<ElementInfo> thisIterator = this.getElements().iterator();
        final Iterator<ElementInfo> targetIterator = o.getElements().iterator();
        while (thisIterator.hasNext() && targetIterator.hasNext()) {
            final int elementOrder = thisIterator.next().compareTo(targetIterator.next());
            if (0 != elementOrder) {
                return elementOrder;
            }
        }
        if (!thisIterator.hasNext() && !targetIterator.hasNext()) {
            return 0;
        }
        if (!thisIterator.hasNext()) {
            return -1;
        }
        if (!targetIterator.hasNext()) {
            return 1;
        }
        assert false : "Here shouldn't be reached!";
        return 0;
    }

    @Override
    public int hashCode() {
        long total = 0;
        for (final ElementInfo element : this.getElements()) {
            total += element.hashCode();
        }
        return (int) total / this.getLength();
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (!(o instanceof CodeCloneInfo)) {
            return false;
        }
        final CodeCloneInfo target = (CodeCloneInfo) o;
        return this.elements.containsAll(target.elements) && target.elements.containsAll(this.elements);
    }

    public static String CODECLONE = new String("CODECLONE");

    private final SortedSet<ElementInfo> elements;

    private final SortedSet<MethodCallInfo> calls;

    private int id;

    private int gap;

    private int method;
}
