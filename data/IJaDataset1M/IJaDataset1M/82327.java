package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * �Q�ƌ^��\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ClassTypeInfo implements ReferenceTypeInfo {

    /**
     * �Q�ƌ^��List���N���X��List�ɕϊ�����
     * 
     * @param references �Q�ƌ^��List
     * @return �N���X��List
     */
    public static List<ClassInfo> convert(final List<ClassTypeInfo> references) {
        final List<ClassInfo> classInfos = new LinkedList<ClassInfo>();
        for (final ClassTypeInfo reference : references) {
            classInfos.add(reference.getReferencedClass());
        }
        return Collections.unmodifiableList(classInfos);
    }

    /**
     * �Q�ƌ^��SortedSet���N���X��SortedSet�ɕϊ�����
     * 
     * @param references �Q�ƌ^��SortedSet
     * @return �N���X��SortedSet
     */
    public static SortedSet<ClassInfo> convert(final SortedSet<ClassTypeInfo> references) {
        final SortedSet<ClassInfo> classInfos = new TreeSet<ClassInfo>();
        for (final ClassTypeInfo reference : references) {
            classInfos.add(reference.getReferencedClass());
        }
        return Collections.unmodifiableSortedSet(classInfos);
    }

    /**
     * �Q�Ƃ����N���X��^���ď���
     * 
     * @param referencedClass �Q�Ƃ����N���X
     */
    public ClassTypeInfo(final ClassInfo referencedClass) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencedClass) {
            throw new NullPointerException();
        }
        this.referencedClass = referencedClass;
        this.typeArguments = new ArrayList<TypeInfo>();
    }

    /**
     * ��ŗ^����ꂽ�^�𓙂������ǂ������r�D
     * 
     * @return �������ꍇ��true�C�������Ȃ��ꍇ��false
     */
    public boolean equals(TypeInfo typeInfo) {
        if (null == typeInfo) {
            return false;
        }
        if (!(typeInfo instanceof ClassTypeInfo)) {
            return false;
        }
        final ClassTypeInfo targetReferenceType = (ClassTypeInfo) typeInfo;
        final ClassInfo targetReferencedClass = targetReferenceType.getReferencedClass();
        if (!this.referencedClass.equals(targetReferencedClass)) {
            return false;
        }
        final List<TypeInfo> thisTypeParameters = this.typeArguments;
        final List<TypeInfo> targetTypeParameters = targetReferenceType.getTypeArguments();
        if (thisTypeParameters.size() != targetTypeParameters.size()) {
            return false;
        }
        final Iterator<TypeInfo> thisTypeParameterIterator = thisTypeParameters.iterator();
        final Iterator<TypeInfo> targetTypeParameterIterator = targetTypeParameters.iterator();
        while (thisTypeParameterIterator.hasNext()) {
            final TypeInfo thisTypeParameter = thisTypeParameterIterator.next();
            final TypeInfo targetTypeParameter = targetTypeParameterIterator.next();
            if (!thisTypeParameter.equals(targetTypeParameter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * ���̎Q�ƌ^��\���������Ԃ�
     * 
     * @return ���̎Q�ƌ^��\��������
     */
    public String getTypeName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.referencedClass.getFullQualifiedName("."));
        if (0 < this.typeArguments.size()) {
            sb.append("<");
            for (final TypeInfo typeParameter : this.typeArguments) {
                sb.append(typeParameter.getTypeName());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(">");
        }
        return sb.toString();
    }

    /**
     * �Q�Ƃ���Ă���N���X��Ԃ�
     * 
     * @return �Q�Ƃ���Ă���N���X
     */
    public ClassInfo getReferencedClass() {
        return this.referencedClass;
    }

    /**
     * ���̎Q�ƌ^�ɗp�����Ă���^��̃��X�g��Ԃ�
     * 
     * @return ���̎Q�ƌ^�ɗp�����Ă���^��̃��X�g��Ԃ�
     */
    public List<TypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * ���̎Q�ƌ^�̃C���f�b�N�X�Ŏw�肳�ꂽ�^���Ԃ�.
     * 
     * @param index �^��̃C���f�b�N�X
     * @return�@���̎Q�ƌ^�̃C���f�b�N�X�Ŏw�肳�ꂽ�^��
     */
    public TypeInfo getTypeArgument(final int index) {
        final ClassInfo referencedClass = this.getReferencedClass();
        final List<TypeParameterInfo> typeParameters = referencedClass.getTypeParameters();
        if ((index < 0) || (typeParameters.size() <= index)) {
            throw new IllegalArgumentException();
        }
        if (index < this.typeArguments.size()) {
            return this.typeArguments.get(index);
        } else {
            final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager().getClassInfo(new String[] { "java", "lang", "Object" });
            return new ClassTypeInfo(objectClass);
        }
    }

    /**
     * ��ŗ^����ꂽ�^�p�����[�^�ɑΉ�����^���Ԃ�
     * @param typeParameter
     * @return
     */
    public TypeInfo getTypeArgument(final TypeParameterInfo typeParameter) {
        final ClassInfo referencedClass = this.getReferencedClass();
        final List<TypeParameterInfo> typeParameters = referencedClass.getTypeParameters();
        if (typeParameters.contains(typeParameter)) {
            final int index = typeParameter.getIndex();
            return this.getTypeArgument(index);
        }
        for (final ClassTypeInfo superClassType : referencedClass.getSuperClasses()) {
            final TypeInfo superTypeArgumentType = superClassType.getTypeArgument(typeParameter);
            if (null == superTypeArgumentType) {
                continue;
            }
            if (superTypeArgumentType instanceof TypeParameterTypeInfo) {
                final TypeParameterInfo superTypeTypeParameter = ((TypeParameterTypeInfo) superTypeArgumentType).getReferncedTypeParameter();
                if (typeParameters.contains(superTypeTypeParameter)) {
                    final int index = typeParameter.getIndex();
                    return this.getTypeArgument(index);
                } else {
                    final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager().getClassInfo(new String[] { "java", "lang", "Object" });
                    return new ClassTypeInfo(objectClass);
                }
            } else {
                return superTypeArgumentType;
            }
        }
        return null;
    }

    /**
     * ���̎Q�ƌ^�Ɍ^���ǉ�
     * 
     * @param argument �ǉ�����^��
     */
    public void addTypeArgument(final TypeInfo argument) {
        this.typeArguments.add(argument);
    }

    /**
     * ���̎Q�ƌ^���\���N���X��ۑ����邽�߂̕ϐ�
     */
    private final ClassInfo referencedClass;

    /**
     * ���̎Q�ƌ^�̌^�p�����[�^��ۑ����邽�߂̕ϐ�
     */
    private final List<TypeInfo> typeArguments;
}
