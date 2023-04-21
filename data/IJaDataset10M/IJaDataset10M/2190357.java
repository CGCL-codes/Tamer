package it.jplag.jplagClient;

public class SetMailTemplateParams {

    protected int type;

    protected it.jplag.jplagClient.MailTemplate template;

    public SetMailTemplateParams() {
    }

    public SetMailTemplateParams(int type, it.jplag.jplagClient.MailTemplate template) {
        this.type = type;
        this.template = template;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public it.jplag.jplagClient.MailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(it.jplag.jplagClient.MailTemplate template) {
        this.template = template;
    }
}
