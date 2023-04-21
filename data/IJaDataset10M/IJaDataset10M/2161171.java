package ru.ipo.dces.pluginapi;

import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.server.ServerFacade;
import ru.ipo.problemsapi.Problem;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Интерфейс, через который Plugin'ы обращаются к основной части клиента.
 * Окружение Plguin'а, которое соответствует одному Plugin'у
 */
public interface PluginEnvironment {

    /**
     * Установить текст на кнопке определенного Plugin'а
     *
     * @param title the title to set
     */
    void setTitle(String title);

    /**
     * Sends information to the server-side plugin
     *
     * @param solution information to send
     * @return recieved information. May return RequestFailedResponse
     * @throws ru.ipo.dces.exceptions.GeneralRequestFailureException
     *          if the serever is inaccessible
     */
    HashMap<String, String> submitSolution(HashMap<String, String> solution) throws GeneralRequestFailureException;

    /**
     * Returns problem to work with
     *
     * @return a problem
     */
    Problem getProblem();

    /**
     * Creates JComponent that displays problem statement
     *
     * @return newly created component to display statement
     * @throws java.io.IOException if failed to read statement from the problem
     */
    public JComponent getStatementPanel() throws IOException;

    /**
     * Returns problem name. The name is displayed on the problem tab
     *
     * @return the name
     * @deprecated no need for plugin to know the name
     */
    String getProblemName();

    void log(String message, LogMessageType type);

    /**
     * @return server to send messages to
     * @deprecated try to avoid
     */
    ServerFacade getServer();

    /**
     * Used with getServer()
     *
     * @return session id
     * @deprecated try to avoid
     */
    String getSessionID();

    /**
     * Used with getServer()
     *
     * @return problem id
     * @deprecated try to avoid
     */
    int getProblemID();

    /**
     * Get folder that contains data to create problem statement
     *
     * @return file, representing a directory of a problem
     * @deprecated use getProblem() instead
     */
    File getProblemFolder();
}
