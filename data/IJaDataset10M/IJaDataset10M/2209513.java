package org.powerfolder.web.pages;

public class UndefinedWebTagBean implements UndefinedWebTag {

    private String namespace = null;

    private String name = null;

    protected UndefinedWebTagBean(String inNamespace, String inName) {
        this.namespace = inNamespace;
        this.name = inName;
    }

    public void initializeWebTag(InitializeWebTagContext inIwtc) {
        WebTagInitializer wti = WebTagInitializerFactory.newInstance(inIwtc);
        wti.initialize();
    }

    public void getWebTagInformation(WebTagInformationContext inWtic) {
        String description = "?(" + this.name + ")?";
        if (this.namespace != null) {
            description = "?(" + this.namespace + ")?" + description;
        }
        if (inWtic instanceof StudioWebTagInstanceInformationContext) {
            StudioWebTagInstanceInformationContext stwiic = (StudioWebTagInstanceInformationContext) inWtic;
            StringBuffer titleSb = new StringBuffer(description);
            StringBuffer descSb = new StringBuffer(description);
            stwiic.setWebTagInstanceTitle(titleSb.toString());
            stwiic.setWebTagInstanceDescription(descSb.toString());
        } else if (inWtic instanceof StudioWebTagTypeInformationContext) {
            StudioWebTagTypeInformationContext swttic = (StudioWebTagTypeInformationContext) inWtic;
            swttic.setWebTagTypeTitle(description);
            swttic.setWebTagTypeDescription(description);
        }
    }
}
