package com.bluesky.my4gl.core.flow.block;

import com.bluesky.my4gl.core.flow.JudgementNode;

public class WhileBlock extends Block {

    private String condition;

    private Block body;

    public WhileBlock(String condition, Block body) {
        this.condition = condition;
        this.body = body;
        generateFlowChart();
    }

    @Override
    protected void generateFlowChart() {
        JudgementNode judge = new JudgementNode(condition);
        inPort.connect(judge);
        judge.connect(true, body);
        judge.connect(false, outPort);
        body.connect(judge);
        addChild(judge);
        addChild(body);
    }
}
