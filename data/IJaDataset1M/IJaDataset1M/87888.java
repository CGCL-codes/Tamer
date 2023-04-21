package net.bpfurtado.tas.runner;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.bpfurtado.tas.model.Game;
import net.bpfurtado.tas.model.Scene;
import org.apache.log4j.Logger;
import bsh.EvalError;
import bsh.Interpreter;

public class CodeExecutionAnalyser {

    private static Logger logger = Logger.getLogger(CodeExecutionAnalyser.class);

    private static Random rnd = new Random();

    public static void main(String[] args) {
        System.out.println(rnd.nextInt(10));
    }

    @SuppressWarnings("unchecked")
    public List<PostCodeExecutionAction> analyseCode(Game game, String code, String currentSceneText) {
        List<PostCodeExecutionAction> actions = new LinkedList<PostCodeExecutionAction>();
        Interpreter interpreter = new Interpreter();
        try {
            interpreter.set("text", currentSceneText);
            interpreter.set("rnd", rnd);
            interpreter.set("originalText", game.getCurrentScene().getOriginalText());
            interpreter.set("origText", game.getCurrentScene().getOriginalText());
            LinkedList<Integer> pathsToHide = new LinkedList<Integer>();
            interpreter.set("pathsToHide", pathsToHide);
            interpreter.set("player", game.getPlayer());
            interpreter.eval(code);
            logger.debug(game.getPlayer());
            Integer go = (Integer) interpreter.get("go");
            if (go != null) {
                logger.debug("will go to " + go);
                actions.add(new SwitchSceneAction(go));
            }
            pathsToHide = (LinkedList<Integer>) interpreter.get("pathsToHide");
            logger.debug(pathsToHide);
            if (!pathsToHide.isEmpty()) {
                actions.add(new HidePaths(pathsToHide));
            }
            String text = (String) interpreter.get("text");
            if (text != null && text.trim().length() > 0) {
                game.getCurrentScene().setText(text);
            }
        } catch (EvalError e) {
            Scene currentScene = game.getCurrentScene();
            String sceneStr = "[Scene: id=" + currentScene.getId() + ", code=" + currentScene.getCode() + ", name=" + currentScene.getName() + "]";
            throw new BadSceneCodeException("Bad Scene " + sceneStr + " code, call the scene author :)", e);
        }
        return actions;
    }
}
