package br.com.gonow.gtt.rpc.client;

import java.util.List;
import br.com.gonow.gtt.exception.TranslationAlreadyExistException;
import br.com.gonow.gtt.exception.UnauthorizedAcessException;
import br.com.gonow.gtt.exception.UserNotFoundException;
import br.com.gonow.gtt.exception.ValidationException;
import br.com.gonow.gtt.model.File;
import br.com.gonow.gtt.model.Language;
import br.com.gonow.gtt.model.LocalUser;
import br.com.gonow.gtt.model.Participation;
import br.com.gonow.gtt.model.Project;
import br.com.gonow.gtt.model.Role;
import br.com.gonow.gtt.model.Translation;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("translationTool")
public interface TranslationToolRpcService extends RemoteService {

    void addLanguage(Project project, String language) throws ValidationException;

    void addSystemLanguage(String description, String locale);

    Translation addTranslation(Translation translation) throws TranslationAlreadyExistException;

    void addUser(Project project, String userId, Role role, String languageId) throws UserNotFoundException, ValidationException;

    void createProject(Project project) throws ValidationException;

    List<File> getFilesOfProject(Project project);

    List<Language> getLanguages();

    List<Language> getLanguagesOfProject(Project project);

    LocalUser getLocalUser();

    List<Participation> getParticipation(Project project, Role role);

    List<Project> getProjects();

    List<Translation> getTranslations(Project project, Language language);

    void removeFile(File file);

    void removeUser(Participation participation) throws UserNotFoundException, ValidationException;

    void updateLocalUser(LocalUser localUser) throws UnauthorizedAcessException;
}
