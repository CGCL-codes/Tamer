package serene.internal;

class NameTask extends RNGParseEndElementTask {

    NameTask() {
        super();
    }

    public void execute() {
        builder.buildName(context.getCharacterContent(), context.getElementInputRecordIndex());
    }
}
