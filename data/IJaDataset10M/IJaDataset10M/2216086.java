package com.vitria.test.rule;

import java.io.InputStream;
import java.io.InputStreamReader;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatelessSession;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;

public class DroolsUtil {

    static {
        System.setProperty("drools.dialect.java.compiler", "JANINO");
    }

    public static void ruleDrools(InputStream is, Object fact) throws Exception {
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        PackageBuilder builder = new PackageBuilder();
        builder.addPackageFromDrl(new InputStreamReader(is));
        Package pkg = builder.getPackage();
        String sum = pkg.getErrorSummary();
        if (sum != null) {
        }
        ruleBase.addPackage(pkg);
        StatelessSession session = ruleBase.newStatelessSession();
        session.execute(fact);
    }
}
