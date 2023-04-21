package ch.unibe.im2.inkanno.imageExport;

public class ImageSimpleDrawer extends RegisteredImageExportDrawer {

    @Override
    public String getDescription() {
        return "Black and white image with only the traces";
    }

    @Override
    public String getId() {
        return "trace_only";
    }
}
