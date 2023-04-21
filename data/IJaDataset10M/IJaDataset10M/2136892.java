package org.makagiga.plugins.contact;

import java.awt.Image;
import java.io.File;
import org.makagiga.commons.Config;
import org.makagiga.commons.MProperties;
import org.makagiga.commons.preview.Preview;
import org.makagiga.editors.Editor;
import org.makagiga.editors.EditorPlugin;
import org.makagiga.plugins.PluginException;
import org.makagiga.plugins.PluginInfo;
import org.makagiga.vcard.VCard;

public final class Plugin extends EditorPlugin {

    @Override
    public Editor<?> create() {
        return new Main();
    }

    @Override
    public void onInit() throws PluginException {
        super.onInit();
        setFileTypes(new FileType("vcf"));
        setImportTypes(new FileType("vcf", getName()));
        addPreview("vcf", new Preview(true) {

            @Override
            public Image getImage(final File file, final int width, final MProperties properties) throws Exception {
                VCard card = new VCard(VCard.VERSION_2_1);
                card.read(file);
                return null;
            }
        });
    }
}
