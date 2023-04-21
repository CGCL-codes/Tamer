package net.sourceforge.solexatools.dao;

import java.util.List;
import net.sourceforge.solexatools.model.Lane;
import net.sourceforge.solexatools.model.Platform;
import net.sourceforge.solexatools.model.StudyType;
import net.sourceforge.solexatools.model.Registration;

public interface PlatformDAO {

    public List<Platform> list(Registration registration);

    public Platform findByID(Integer id);
}
