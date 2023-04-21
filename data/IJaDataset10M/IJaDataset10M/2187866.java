package net.sourceforge.taggerplugin.model;

class TagAssociationTestFixture {

    static final String ASSOCA_ID = "/projecta";

    static final String ASSOCB_ID = "/projectb";

    TagAssociation assocA, assocB;

    TagAssociationTestFixture() {
        super();
    }

    void init(TagTestFixture ttf) {
        assocA = new TagAssociation(ASSOCA_ID);
        assocA.addTag(ttf.tagA);
        assocB = new TagAssociation(ASSOCB_ID);
        assocB.addTag(ttf.tagA);
        assocB.addTag(ttf.tagB);
    }

    void destroy() {
        assocA = null;
        assocB = null;
    }
}
