package ru.megadevelopers.xboard.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.concurrent.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.megadevelopers.xboard.core.Settings;
import ru.megadevelopers.xboard.dao.MessageDao;
import ru.megadevelopers.xboard.managers.TopicManager;

@Controller
public class ExploreController {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private TopicManager topicManager;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private Settings settings;

    @RequestMapping
    public void handleRequest(ModelMap map) {
        map.addAttribute("topics", topicManager.getRankedTopics().values());
        map.addAttribute("users", sessionRegistry.getAllPrincipals());
        map.addAttribute("threads", messageDao.loadLastThreads(settings.getThreadsToShow()));
    }
}
