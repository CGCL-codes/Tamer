package com.googlecode.multiagentsys.agent.factory;

import com.googlecode.multiagentsys.agent.AbstractAgent;
import com.googlecode.multiagentsys.agent.HumanAgent;
import com.googlecode.multiagentsys.intention.factory.IntentionGotoFactory;

/**
 * ����� ��������� IAbstractAgentFactory (������� �������). ������ ����� ���������������� �������
 * @author Tom-Trix
 */
public class HumanAgentFactory implements IAbstractAgentFactory {

    @Override
    public AbstractAgent NewAbstractAgent() {
        return new HumanAgent(new IntentionGotoFactory());
    }
}
