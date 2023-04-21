package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class StaticInitializerStateManager extends CallableUnitStateManager {

    /**
     * ��̃C�x���g���X�^�e�B�b�N�E�C�j�V�����C�U��`����\�����ǂ�������Ԃ�
     */
    @Override
    protected boolean isDefinitionEvent(AstVisitEvent event) {
        return event.getToken().isStaticInitializerDefinition();
    }
}
