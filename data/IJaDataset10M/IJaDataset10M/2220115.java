package de.uni_leipzig.lots.webfrontend.actions;

import de.uni_leipzig.lots.common.objects.Group;
import de.uni_leipzig.lots.common.objects.User;
import de.uni_leipzig.lots.common.objects.Role;
import de.uni_leipzig.lots.common.objects.training.TrainingPaper;
import de.uni_leipzig.lots.common.objects.training.UserTrainingPaper;
import de.uni_leipzig.lots.server.persist.TrainingPaperRepository;
import de.uni_leipzig.lots.server.persist.UserTrainingPaperRepository;
import de.uni_leipzig.lots.webfrontend.formbeans.BaseForm;
import de.uni_leipzig.lots.webfrontend.http.LOTSHttpSession;
import de.uni_leipzig.lots.webfrontend.views.PageData;
import de.uni_leipzig.lots.webfrontend.container.UserContainer;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * TrainingManagerAction Action
 *
 * @author Alexander Kiel
 * @version $Id: TrainingManagerAction.java,v 1.17 2007/10/23 06:29:56 mai99bxd Exp $
 */
public final class TrainingManagerAction extends AdministrationAction {

    private TrainingPaperRepository trainingPaperRepository;

    private UserTrainingPaperRepository userTrainingPaperRepository;

    @Required
    public void setTrainingPaperRepository(@NotNull TrainingPaperRepository trainingPaperRepository) {
        this.trainingPaperRepository = trainingPaperRepository;
    }

    @Required
    public void setUserTrainingPaperRepository(@NotNull UserTrainingPaperRepository userTrainingPaperRepository) {
        this.userTrainingPaperRepository = userTrainingPaperRepository;
    }

    @Nullable
    @Override
    protected ActionForward execute(ActionMapping mapping, BaseForm form, LOTSHttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserContainer userContainer = getUserContainer(session);
        User currentUser = userContainer.getUser();
        String path = mapping.getPath();
        if (path.endsWith("training/")) {
            List<TrainingPaper> trainingPapers;
            if (userContainer.hasCurrentRole(Role.admin)) {
                trainingPapers = trainingPaperRepository.getAll();
            } else {
                Set<Group> groups = currentUser.getGroups();
                if (groups.isEmpty()) {
                    trainingPapers = Collections.emptyList();
                } else {
                    trainingPapers = trainingPaperRepository.getAllWithGroups(groups);
                }
            }
            List<UserTrainingPaper> recentlyChangedUTPs;
            if (userContainer.hasCurrentRole(Role.admin)) {
                recentlyChangedUTPs = userTrainingPaperRepository.getRecentlyChanged();
            } else {
                Set<Group> groups = currentUser.getGroups();
                if (groups.isEmpty()) {
                    recentlyChangedUTPs = Collections.emptyList();
                } else {
                    recentlyChangedUTPs = userTrainingPaperRepository.getRecentlyChangedOfGroups(currentUser.getGroups(), 10);
                }
            }
            request.setAttribute("pageData", new StartPageData(trainingPapers, recentlyChangedUTPs));
        }
        return mapping.findForward("page");
    }

    public static class StartPageData implements PageData {

        private final Collection<TrainingPaper> trainingPapers;

        private final Collection<UserTrainingPaper> recentlyChangedUserTrainingPapers;

        public StartPageData(Collection<TrainingPaper> trainingPapers, Collection<UserTrainingPaper> recentlyChangedUserTrainingPapers) {
            this.trainingPapers = trainingPapers;
            this.recentlyChangedUserTrainingPapers = recentlyChangedUserTrainingPapers;
        }

        public Collection<TrainingPaper> getTrainingPapers() {
            return trainingPapers;
        }

        public Collection<UserTrainingPaper> getRecentlyChangedUserTrainingPapers() {
            return recentlyChangedUserTrainingPapers;
        }
    }
}
