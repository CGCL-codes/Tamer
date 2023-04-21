package net.sf.brightside.aikido.tapestry.util;

import java.util.ArrayList;
import java.util.List;
import net.sf.brightside.aikido.metamodel.Club;
import org.apache.tapestry.OptionGroupModel;
import org.apache.tapestry.OptionModel;
import org.apache.tapestry.util.AbstractSelectModel;

public class ClubSelectModel extends AbstractSelectModel {

    private List<Club> listClub;

    public ClubSelectModel(List<Club> listClub) {
        this.listClub = listClub;
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> list = new ArrayList<OptionModel>();
        for (Club c : listClub) {
            list.add(new ClubOptionModel(c));
        }
        return list;
    }
}
