package ast.vo;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Sep 20, 2011
 */
public class ItemContent {

    private String content;

    private String state;

    private String hasproblem;

    private String solution;

    private String hasrequired;

    public ItemContent() {
    }

    public ItemContent(String _content, String _state, String _hasproblem, String _solution, String _hasrequired) {
        this.setContent(_content);
        this.setState(_state);
        this.setHasproblem(_hasproblem);
        this.setSolution(_solution);
        this.setHasrequired(_hasrequired);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getHasproblem() {
        return hasproblem;
    }

    public void setHasproblem(String hasproblem) {
        this.hasproblem = hasproblem;
    }

    public String getHasrequired() {
        return hasrequired;
    }

    public void setHasrequired(String hasrequired) {
        this.hasrequired = hasrequired;
    }

    @Override
    public String toString() {
        return "ItemContent [content=" + content + ", state=" + state + ", hasproblem=" + hasproblem + ", solution=" + solution + ", hasrequired=" + hasrequired + "]";
    }
}
