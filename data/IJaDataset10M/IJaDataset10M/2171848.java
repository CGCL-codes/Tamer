package com.crypticbit.ipa.ui.swing.plist;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.Collection;
import com.crypticbit.ipa.central.FileParseException;
import com.crypticbit.ipa.central.backupfile.BackupFile;
import com.crypticbit.ipa.io.parser.plist.PListResults;
import com.crypticbit.ipa.results.ContentType;
import com.crypticbit.ipa.results.Location;
import com.crypticbit.ipa.ui.swing.Mediator;
import com.crypticbit.ipa.ui.swing.Mediator.HighlightChangeListener;
import com.crypticbit.ipa.ui.swing.View;

@SuppressWarnings("serial")
public class PListView extends View implements HighlightChangeListener {

    private PListPanel panel;

    public PListView(final BackupFile bfd, final Mediator mediator) throws IOException, FileParseException {
        super(bfd, mediator);
        setLayout(new GridLayout(1, 1));
    }

    @Override
    public void clearHighlighting() {
        this.panel.clearHighlighting();
    }

    @Override
    public void highlight(final Collection<Location> locations) {
        this.panel.highlight(locations);
    }

    @Override
    protected ContentType getSupportedContentView() {
        return ContentType.PLIST;
    }

    @Override
    protected void init() throws IOException, FileParseException {
        PListResults<?> plp = (PListResults<?>) getBackupFile().getParsedData();
        this.panel = new PListPanel(plp.getRootContainer(), getMediator());
        this.add(this.panel);
    }

    @Override
    protected void moveTo0(final Location location) {
        this.panel.moveTo(location);
    }

    @Override
    protected boolean shouldBeVisible() {
        return getBackupFile().getParsedData() instanceof PListResults;
    }
}
