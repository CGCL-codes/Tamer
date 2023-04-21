package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * ������ switch �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class UnresolvedSwitchBlockInfo extends UnresolvedConditionalBlockInfo<SwitchBlockInfo> {

    /**
     * �O���̃u���b�N����^���āCswitch �u���b�N��������
     * 
     * @param outerSpace �O���̃u���b�N
     */
    public UnresolvedSwitchBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    /**
     * ���̖����� switch �u���b�N����������
     * 
     * @param usingClass �����N���X
     * @param usingMethod �������\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     */
    @Override
    public SwitchBlockInfo resolve(final TargetClassInfo usingClass, final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (this.alreadyResolved()) {
            return this.getResolved();
        }
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();
        this.resolvedInfo = new SwitchBlockInfo(fromLine, fromColumn, toLine, toColumn);
        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);
        return this.resolvedInfo;
    }
}
