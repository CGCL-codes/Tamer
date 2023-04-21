package net.sf.gham.core.entity.loaders;

import java.io.IOException;
import java.util.List;
import net.sf.gham.core.entity.team.myteam.IMyTeam;
import net.sf.gham.core.entity.team.myteam.MyTeam;
import net.sf.gham.swing.DoubleProgressDialog;

/**
 * @author fabio
 *
 */
public abstract class DbDataLoader extends DataLoader {

    public DbDataLoader() {
    }

    @Override
    public final void load(final MyTeam team, final DoubleProgressDialog dlg, final String teamDir, final List<String> downloadedFiles) throws IOException {
        initPreLoad(team, teamDir);
        loadFromFiles(team, dlg, teamDir);
        initPostLoad(team);
    }

    protected abstract boolean loadFromFiles(MyTeam team, DoubleProgressDialog dlg, String teamDir, List<String> downloadedFiles) throws IOException;

    @SuppressWarnings("unused")
    protected void initPreLoad(IMyTeam team, String teamDir) {
    }

    /**
	 * @param team
	 */
    protected void initPostLoad(@SuppressWarnings("unused") MyTeam team) {
    }

    protected abstract void loadFromFiles(MyTeam team, DoubleProgressDialog dlg, String teamDir) throws IOException;
}
