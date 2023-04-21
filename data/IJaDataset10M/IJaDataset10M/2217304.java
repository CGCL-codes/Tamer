package au.edu.uq.itee.maenad.pronto.dataaccess;

import au.edu.uq.itee.maenad.dataaccess.Dao;
import au.edu.uq.itee.maenad.pronto.model.Comment;
import au.edu.uq.itee.maenad.pronto.model.Ontology;
import au.edu.uq.itee.maenad.pronto.model.OntologyVersion;
import au.edu.uq.itee.maenad.pronto.model.User;
import java.util.List;

public interface UserDao extends Dao<User> {

    List<Ontology> getOntologyEntriesCreated(User user);

    List<OntologyVersion> getOntologyVersionsSubmitted(User user);

    List<Comment> getOntologyComments(User user);
}
