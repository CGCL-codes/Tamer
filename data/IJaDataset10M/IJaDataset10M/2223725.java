package de.fzj.rg;

import de.fzj.rg.result.Result;

public abstract class Command {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public abstract void init(PWD pwd);

    public abstract void visit(PWD pwd);
}
