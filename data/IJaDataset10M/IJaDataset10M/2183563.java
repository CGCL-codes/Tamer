package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * �N���X�̏���ۗL����N���X�D�ȉ��̏������D
 * <ul>
 * <li>�N���X��</li>
 * <li>�C��q</li>
 * <li>���O��ԁi�p�b�P�[�W���j</li>
 * <li>�s��</li>
 * <li>�p�����Ă���N���X</li>
 * <li>�p������Ă���N���X</li>
 * <li>�Q�Ƃ��Ă���N���X</li>
 * <li>�Q�Ƃ���Ă���N���X</li>
 * <li>�����N���X</li>
 * <li>���̃N���X���Œ�`����Ă��郁�\�b�h</li>
 * <li>���̃N���X���Œ�`����Ă���t�B�[���h</li>
 * </ul>
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class TargetClassInfo extends ClassInfo {

    /**
     * �w�肳�ꂽ�N���X�Ɋ܂܂��S�ẴC���i�[�N���X��Ԃ�
     * 
     * @param classInfo �w�肷��N���X
     * @return�@�w�肳�ꂽ�N���X�Ɋ܂܂��S�ẴC���i�[�N���X
     */
    public static SortedSet<ClassInfo> getAllInnerClasses(final ClassInfo classInfo) {
        if (null == classInfo) {
            throw new IllegalArgumentException();
        }
        final SortedSet<ClassInfo> innerClassInfos = new TreeSet<ClassInfo>();
        for (final InnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            innerClassInfos.add((ClassInfo) innerClassInfo);
            innerClassInfos.addAll(getAllInnerClasses((ClassInfo) innerClassInfo));
        }
        return Collections.unmodifiableSortedSet(innerClassInfos);
    }

    /**
     * �w�肵���N���X�ɂ����ăA�N�Z�X�\�ȃC���i�[�N���X�ꗗ��Ԃ��D
     * �A�N�Z�X�\�ȃN���X�Ƃ́C�w�肳�ꂽ�N���X�C�������͂��̐e�N���X���ɒ�`���ꂽ�N���X����D
     * ��x�ŊO���N���X�܂ł��ǂ��āC���̓����N���X�C�e�N���X���`�F�b�N����.
     * 
     * @param classInfo �w�肳�ꂽ�N���X
     * @return �w�肵���N���X�ɂ����ăA�N�Z�X�\�ȃC���i�[�N���X�ꗗ��Ԃ��D
     */
    public static SortedSet<ClassInfo> getAccessibleInnerClasses(final ClassInfo classInfo) {
        if (null == classInfo) {
            throw new IllegalArgumentException();
        }
        final SortedSet<ClassInfo> classCache = new TreeSet<ClassInfo>();
        if (classInfo instanceof InnerClassInfo) {
            final ClassInfo outestClass = TargetInnerClassInfo.getOutestClass((InnerClassInfo) classInfo);
            return Collections.unmodifiableSortedSet(getAccessibleInnerClasses(outestClass, classCache));
        } else {
            return Collections.unmodifiableSortedSet(getAccessibleInnerClasses(classInfo, classCache));
        }
    }

    private static SortedSet<ClassInfo> getAccessibleInnerClasses(final ClassInfo classInfo, final SortedSet<ClassInfo> classCache) {
        if ((null == classInfo) || (null == classCache)) {
            throw new IllegalArgumentException();
        }
        if (classCache.contains(classInfo)) {
            return Collections.unmodifiableSortedSet(new TreeSet<ClassInfo>());
        }
        classCache.add(classInfo);
        final SortedSet<ClassInfo> innerClassInfos = new TreeSet<ClassInfo>();
        for (final InnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            innerClassInfos.add((ClassInfo) innerClassInfo);
            innerClassInfos.addAll(getAccessibleInnerClasses((ClassInfo) innerClassInfo, classCache));
        }
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            if (superClassInfo instanceof InnerClassInfo) {
                innerClassInfos.add(superClassInfo);
            }
            innerClassInfos.addAll(getAccessibleInnerClasses(superClassInfo, classCache));
        }
        return Collections.unmodifiableSortedSet(innerClassInfos);
    }

    /**
     * ���O��Ԗ��C�N���X����^���ăN���X���I�u�W�F�N�g������
     * 
     * @param modifiers �C��q�� Set
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     * @param isInterface �C���^�[�t�F�[�X���ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace, final String className, final boolean isInterface, final boolean isEnum, final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, namespace, className, isInterface, isEnum, fromLine, fromColumn, toLine, toColumn);
        if (null == modifiers) {
            throw new NullPointerException();
        }
        this.implicitInstanceInitializer = new InstanceInitializerInfo(0, 0, 0, 0);
        this.implicitInstanceInitializer.setOuterUnit(this);
        this.implicitStaticInitializer = new StaticInitializerInfo(0, 0, 0, 0);
        this.implicitStaticInitializer.setOuterUnit(this);
        this.instanceInitializers = new TreeSet<InstanceInitializerInfo>();
        this.instanceInitializers.add(this.implicitInstanceInitializer);
        this.staticInitializers = new TreeSet<StaticInitializerInfo>();
        this.importStatements = new TreeSet<ImportStatementInfo<?>>();
        this.availableTypeParameters = new HashMap<TypeParameterInfo, TypeInfo>();
        this.ownerFile = fileInfo;
    }

    /**
     * ���S���薼��^���āC�N���X���I�u�W�F�N�g������
     * 
     * @param modifiers �C��q�� Set
     * @param fullQualifiedName ���S���薼
     * @param isInterface �C���^�t�F�[�X�ł��邩�ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName, final boolean isInterface, final boolean isEnum, final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, fullQualifiedName, isInterface, isEnum, fromLine, fromColumn, toLine, toColumn);
        if (null == modifiers || null == fileInfo) {
            throw new NullPointerException();
        }
        this.implicitInstanceInitializer = new InstanceInitializerInfo(0, 0, 0, 0);
        this.implicitInstanceInitializer.setOuterUnit(this);
        this.implicitStaticInitializer = new StaticInitializerInfo(0, 0, 0, 0);
        this.implicitStaticInitializer.setOuterUnit(this);
        this.instanceInitializers = new TreeSet<InstanceInitializerInfo>();
        this.staticInitializers = new TreeSet<StaticInitializerInfo>();
        this.importStatements = new TreeSet<ImportStatementInfo<?>>();
        this.availableTypeParameters = new HashMap<TypeParameterInfo, TypeInfo>();
        this.ownerFile = fileInfo;
    }

    /**
     * �C���X�^���X�C�j�V�����C�U�[��ǉ�����
     * 
     * @param instanceInitializer �ǉ������C���X�^���X�C�j�V�����C�U�[
     */
    public final void addInstanceInitializer(final InstanceInitializerInfo instanceInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == instanceInitializer) {
            throw new NullPointerException();
        }
        this.instanceInitializers.add(instanceInitializer);
    }

    /**
     * �X�^�e�B�b�N�C�j�V�����C�U�[��ǉ�����
     * 
     * @param staticInitializer �ǉ������X�^�e�B�b�N�C�j�V�����C�U�[
     */
    public final void addStaticInitializer(final StaticInitializerInfo staticInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == staticInitializer) {
            throw new NullPointerException();
        }
        this.staticInitializers.add(staticInitializer);
    }

    public final void addImportStatement(final ImportStatementInfo<?> importStatement) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == importStatement) {
            throw new NullPointerException();
        }
        this.importStatements.add(importStatement);
    }

    /**
     * ���̃N���X�̈Öق̃C���X�^���X�C�j�V�����C�U��Ԃ�
     * @return �Öق̃C���X�^���X�C�j�V�����C�U
     */
    public InstanceInitializerInfo getImplicitInstanceInitializer() {
        return this.implicitInstanceInitializer;
    }

    /**
     * ���̃N���X�̃C���X�^���X�C�j�V�����C�U�ꗗ��Ԃ�
     * @return ���̃N���X�̃C���X�^���X�C�j�V�����C�U�ꗗ
     */
    public SortedSet<InstanceInitializerInfo> getInstanceInitializers() {
        return this.instanceInitializers;
    }

    /**
     * ���̃N���X�̈Öق̃X�^�e�B�b�N�C�j�V�����C�U��Ԃ�
     * @return �Öق̃X�^�e�B�b�N�C�j�V�����C�U
     */
    public StaticInitializerInfo getImplicitStaticInitializer() {
        return this.implicitStaticInitializer;
    }

    /**
     * ���̃N���X�̃X�^�e�B�b�N�C�j�V�����C�U�ꗗ��Ԃ�
     * @return �X�^�e�B�b�N�C�j�V�����C�U�ꗗ
     */
    public SortedSet<StaticInitializerInfo> getStaticInitializers() {
        return this.staticInitializers;
    }

    /**
     * ���̃N���X���ɂ�����ϐ��g�p��Set��Ԃ�
     * 
     * @return ���̃N���X���ɂ�����ϐ��g�p��Set
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        for (final MethodInfo definedMethod : this.getDefinedMethods()) {
            variableUsages.addAll(definedMethod.getVariableUsages());
        }
        for (final ConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            variableUsages.addAll(definedConstructor.getVariableUsages());
        }
        for (final InnerClassInfo innerClass : this.getInnerClasses()) {
            variableUsages.addAll(((ClassInfo) innerClass).getVariableUsages());
        }
        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * ���̃N���X���Œ�`����Ă���ϐ���Set��Ԃ�
     * 
     * @return ���̃N���X���Œ�`����Ă���ϐ���Set
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.addAll(this.getDefinedFields());
        for (final MethodInfo definedMethod : this.getDefinedMethods()) {
            definedVariables.addAll(definedMethod.getDefinedVariables());
        }
        for (final ConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            definedVariables.addAll(definedConstructor.getDefinedVariables());
        }
        for (final InnerClassInfo innerClass : this.getInnerClasses()) {
            definedVariables.addAll(((ClassInfo) innerClass).getDefinedVariables());
        }
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * ���̃N���X���ɂ�����Ăяo����Set��Ԃ�
     * 
     * @return ���̃N���X���ɂ�����Ăяo����Set
     */
    @Override
    public final Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        final Set<CallInfo<? extends CallableUnitInfo>> calls = new HashSet<CallInfo<? extends CallableUnitInfo>>();
        for (final MethodInfo definedMethod : this.getDefinedMethods()) {
            calls.addAll(definedMethod.getCalls());
        }
        for (final ConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            calls.addAll(definedConstructor.getCalls());
        }
        for (final InnerClassInfo innerClass : this.getInnerClasses()) {
            calls.addAll(((ClassInfo) innerClass).getCalls());
        }
        return Collections.unmodifiableSet(calls);
    }

    /**
     * ���̃N���X�ɂ����闘�p�\�Ȍ^�p�����[�^�Ƃ��̎��ۂ̌^��o�^����
     * 
     * @param map
     */
    public void addAvailableTypeParameters(final Map<TypeParameterInfo, TypeInfo> map) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.availableTypeParameters.putAll(map);
    }

    /**
     * ���̃N���X�ɂ����闘�p�\�Ȍ^�p�����[�^�Ƃ��̎��ۂ̌^��o�^����
     * 
     * @param map
     */
    public void addAvailableTypeParameter(final TypeParameterInfo typeParameter, TypeInfo type) {
        this.availableTypeParameters.put(typeParameter, type);
    }

    public Map<TypeParameterInfo, TypeInfo> getAvailableTypeParameters() {
        final Map<TypeParameterInfo, TypeInfo> map = new HashMap<TypeParameterInfo, TypeInfo>();
        for (final ClassTypeInfo superClassType : this.getSuperClasses()) {
            final ClassInfo superClass = superClassType.getReferencedClass();
            if (superClass instanceof TargetClassInfo) {
                map.putAll(((TargetClassInfo) superClass).getAvailableTypeParameters());
            }
        }
        map.putAll(this.availableTypeParameters);
        return map;
    }

    /**
     * ���̃N���X��錾���Ă���t�@�C������Ԃ�
     * 
     * @return ���̃N���X��錾���Ă���t�@�C�����
     */
    public final FileInfo getOwnerFile() {
        return this.ownerFile;
    }

    /**
     * ���̃N���X�̃X�^�e�B�b�N�C�j�V�����C�U�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<StaticInitializerInfo> staticInitializers;

    /**
     * ���̃N���X�̃C���X�^���X�C�j�V�����C�U�ꗗ��ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<InstanceInitializerInfo> instanceInitializers;

    /**
     * ���̃N���X�̈Öق̃C���X�^���X�C�j�V�����C�U��ۑ����邽�߂̕ϐ�
     */
    private final InstanceInitializerInfo implicitInstanceInitializer;

    /**
     * ���̃N���X�̈Öق̃X�^�e�B�b�N�C�j�V�����C�U��ۑ����邽�߂̕ϐ�
     */
    private final StaticInitializerInfo implicitStaticInitializer;

    private final SortedSet<ImportStatementInfo<?>> importStatements;

    /**
     * ���̃N���X��錾���Ă���t�@�C������ۑ����邽�߂̕ϐ�
     */
    private final FileInfo ownerFile;

    /**
     * ���̃N���X�ŗ��p���Ă���^�p�����[�^�̎��ۂ̌^����ۑ����邽�߂̕ϐ�
     */
    private final Map<TypeParameterInfo, TypeInfo> availableTypeParameters;
}
