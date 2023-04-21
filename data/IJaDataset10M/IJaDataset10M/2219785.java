package net.sourceforge.solexatools.dao;

import java.util.List;
import net.sourceforge.solexatools.model.LibrarySelection;
import net.sourceforge.solexatools.model.LibrarySource;
import net.sourceforge.solexatools.model.LibraryStrategy;
import net.sourceforge.solexatools.model.Platform;
import net.sourceforge.solexatools.model.StudyType;
import net.sourceforge.solexatools.model.Registration;

public interface LibraryStrategyDAO {

    public List<LibraryStrategy> list(Registration registration);

    public LibraryStrategy findByID(Integer id);
}
