package ail.mas;

import org.junit.Test;
import org.junit.Assert;
import ajpf.MCAPLcontroller;
import ail.syntax.Action;
import ail.syntax.VarTerm;
import ail.syntax.Structure;
import eass.EASSMASBuilder;
import eass.verification.leo.LEOVerificationEnvironment;

public class MASTests {

    @Test
    public void lastActionWasTest() {
        String filename = "/src/examples/eass/verification/leo/satellite.eass";
        String abs_filename = MCAPLcontroller.getAbsFilename(filename);
        MAS mas = (new EASSMASBuilder(abs_filename, true)).getMAS();
        LEOVerificationEnvironment env = new LEOVerificationEnvironment();
        mas.setEnv(env);
        Action envaction = new Action("does");
        envaction.addTerm(new VarTerm("P"));
        Action agentaction = new Action("does");
        agentaction.addTerm(new Structure("something"));
        env.lastAction = envaction;
        env.lastAgent = "ag1";
        Assert.assertTrue(mas.lastActionWas("ag1", agentaction));
    }
}
