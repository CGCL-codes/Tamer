package org.cyberaide.gridshell.commands.job.test;

import org.cyberaide.gridshell.commands.job.Qbets;

public class QbetsTest {

    public static void main(String[] a) {
        String argsString = "-hosts datastar ncsateragrid lonestar -nodes 8 -walltime 512";
        Qbets qbets = new Qbets(argsString);
        qbets.execute();
    }
}
