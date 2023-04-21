package org.cheetahworkflow.example.collatz;

import org.cheetahworkflow.engine.WorkflowNode;

public class OddNumberHandler implements WorkflowNode<Context> {

    @Override
    public void exec(Context context) {
        int x = context.get();
        x = x * 3 + 1;
        context.set(x);
        System.out.println(x);
    }
}
