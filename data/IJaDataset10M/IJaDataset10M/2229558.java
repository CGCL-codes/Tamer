package br.com.mendsoft.gtalk.bot.fs.commands;

import java.io.File;

/**
 * @author danielmend
 *
 */
public class LLCommand extends ACommand {

    public String execute(String argCommand) throws Exception {
        String text = "";
        String dirName = argCommand;
        dirName = getPathCurrent() + dirName;
        File diretorio = new File(dirName);
        if (diretorio.isDirectory()) {
            for (File file : diretorio.listFiles()) {
                String tipo = "";
                if (file.isDirectory()) {
                    tipo = "(D)";
                } else {
                    tipo = "(A)";
                }
                text += tipo + " " + file.getName() + "\n";
            }
        } else {
            text = "Diretorio n�o encontrado. [" + dirName + "]";
        }
        return text;
    }
}
