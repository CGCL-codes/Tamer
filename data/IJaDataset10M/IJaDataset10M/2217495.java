package org.slasoi.gslam.pac;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.RuleBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.impl.KnowledgeBaseImpl;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.rule.Package;
import org.drools.rule.Rule;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slasoi.gslam.core.control.Context;
import org.slasoi.gslam.core.control.Policy;

/**
 * Class wrapping the Drools knowledge base, shared between the agents.
 * 
 * @author Beatriz Fuentes (TID)
 * 
 */
public class SharedKnowledgePlane {

    /**
     * Logger for this class
     */
    private static Logger logger = Logger.getLogger(SharedKnowledgePlane.class);

    private static final String RULE = "rule";

    private static final String END = "end";

    private static final char QUOTES = '\"';

    private static final int MAX_POLICIES = 1000;

    private static final String DEBUG = "DEBUG";

    private long idPolicy = 1;

    private boolean initialized = false;

    private boolean created = false;

    /**
     * list that keeps the already loaded files. This will avoid to load twice the same file.
     */
    private Set<String> listRulesFiles;

    /**
     * Singleton
     */
    private static final Map<String, SharedKnowledgePlane> INSTANCES = new HashMap<String, SharedKnowledgePlane>();

    private KnowledgeBuilder kbuilder;

    private KnowledgeBase kbase;

    private StatefulKnowledgeSession ksession;

    private SharedKnowledgePlane() {
        logger.debug("Building SharedKnowledgePlane..." + this.toString());
        synchronized (this) {
            if (!created) {
                logger.debug("Creating KB...");
                kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
                listRulesFiles = new HashSet<String>();
                created = true;
            }
        }
    }

    public static SharedKnowledgePlane getInstance(String slamId) {
        if (!SharedKnowledgePlane.INSTANCES.containsKey(slamId)) {
            SharedKnowledgePlane.INSTANCES.put(slamId, new SharedKnowledgePlane());
        }
        return INSTANCES.get(slamId);
    }

    public synchronized void initKnowledgeBase(String logMode) {
        logger.debug("SharedKnowledgePlane " + this.toString() + ", initKnowledgeBase");
        if (!initialized) {
            logger.debug("Initializing kb...");
            kbase = KnowledgeBaseFactory.newKnowledgeBase();
            kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
            if (ksession == null) ksession = kbase.newStatefulKnowledgeSession();
            if (logMode.equals(DEBUG)) {
                @SuppressWarnings("unused") KnowledgeRuntimeLogger droolsLogger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
            }
            initialized = true;
        }
    }

    public synchronized void initKnowledgeBase() {
        logger.debug("SharedKnowledgePlane " + this.toString() + ", initKnowledgeBase without arguments");
        if (!initialized) {
            kbase = KnowledgeBaseFactory.newKnowledgeBase();
            kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
            if (ksession == null) ksession = kbase.newStatefulKnowledgeSession();
            initialized = true;
        }
    }

    public StatefulKnowledgeSession getStatefulSession() {
        return ksession;
    }

    public synchronized boolean addRulesFile(String file, ResourceType type) {
        boolean success = true;
        logger.debug(this.toString() + ", adding file to knowledge base: " + file);
        if (!listRulesFiles.contains(file)) {
            logger.debug("Adding new file to kb");
            listRulesFiles.add(file);
            kbuilder.add(ResourceFactory.newClassPathResource(file, getClass()), ResourceType.DRL);
            logger.debug("Resource has been added");
            if (kbuilder.hasErrors()) {
                logger.info("KnowledgeBuilder has errors: ");
                logger.info(kbuilder.getErrors().toString());
                success = false;
            } else {
                logger.debug("KnowledgeBuilder without errors, adding now Knowledge Packages...");
                kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
            }
        }
        return (success);
    }

    public StatelessKnowledgeSession getStatelessSession() {
        StatelessKnowledgeSession newSession = kbase.newStatelessKnowledgeSession();
        @SuppressWarnings("unused") KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newConsoleLogger(newSession);
        return newSession;
    }

    public Policy[] getPolicies() {
        logger.debug("Shared KnowledgePlane PAC, getting policies ");
        List<Policy> policies = new ArrayList<Policy>();
        Iterator<String> iter = listRulesFiles.iterator();
        policies = createPoliciesFromRules();
        return ((Policy[]) policies.toArray(new Policy[policies.size()]));
    }

    private List<Policy> getRulesFromFile(List<Policy> policies, String filename) {
        logger.debug("Getting rules from file " + filename);
        String content = readFile(filename);
        int indexend = 0;
        int index = content.indexOf(RULE, indexend);
        while (index != -1) {
            indexend = content.indexOf(END, index);
            String rule = content.substring(index, indexend + END.length());
            policies.add(createPolicy(rule, idPolicy));
            index = content.indexOf(RULE, indexend);
            idPolicy++;
        }
        return policies;
    }

    private Policy createPolicy(String rule, long idPolicy) {
        logger.debug("Creating policy from " + rule);
        int index = rule.indexOf(QUOTES);
        int indexend = rule.indexOf(QUOTES, index + 1);
        String ruleName = rule.substring(index + 1, indexend);
        Policy policy = new Policy();
        policy.setId(idPolicy);
        policy.setName(ruleName);
        policy.setRule(rule);
        return policy;
    }

    private String readFile(String fileName) {
        String content = new String();
        try {
            InputStream in = getClass().getResourceAsStream(fileName);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                content = content + line + System.getProperty("line.separator");
            }
            br.close();
            isr.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public List<Policy> createPoliciesFromRules() {
        logger.debug("Shared KnowledgePlane PAC, getting policies ");
        List<Policy> list = new ArrayList<Policy>();
        Collection<KnowledgePackage> kpackages = kbase.getKnowledgePackages();
        RuleBase rbase = ((KnowledgeBaseImpl) kbase).ruleBase;
        for (Package kp : rbase.getPackages()) {
            logger.debug("Processing package " + kp.getName());
            Rule[] rules = kp.getRules();
            for (Rule rule : rules) {
                logger.debug("Processing rule: ");
                logger.debug("Rule name: " + rule.getName());
                logger.debug("Package definition: " + rule.getPackageName());
                logger.debug("Rule name: " + rule.getMetaAttribute("name"));
                logger.debug("Rule id = " + rule.getMetaAttribute("id"));
                logger.debug("Rule body =" + rule.getMetaAttribute("rulebody"));
                Policy policy = new Policy();
                if (rule.getMetaAttribute("id") != null) policy.setId(Long.parseLong(rule.getMetaAttribute("id")));
                policy.setName(rule.getMetaAttribute("name"));
                policy.setRule(rule.getMetaAttribute("rulebody"));
                Context context = new Context();
                if (rule.getMetaAttribute("ContextId") != null) context.setId(Long.parseLong(rule.getMetaAttribute("ContextId")));
                context.setName(rule.getMetaAttribute("ContextName"));
                context.setContent(rule.getMetaAttribute("ContextContent"));
                policy.setContext(context);
                list.add(policy);
            }
        }
        return list;
    }

    public int setPolicies(Policy[] policies) {
        logger.info("Shared KnowledgePlane PAC, setting policies: " + policies);
        for (Policy policy : policies) {
            logger.debug("Policy id " + policy.getId());
            logger.debug("Policy name " + policy.getName());
            logger.debug("Rule " + policy.getRule());
        }
        String rules = Policies2RulesFile(policies);
        Iterator<String> iter = listRulesFiles.iterator();
        String filename = iter.next();
        logger.info(filename);
        try {
            cleanKnowledgeBase();
            String newfile = getFullPathName(filename);
            FileWriter fileWriter = new FileWriter(newfile);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write(rules);
            out.close();
            addRulesFile(filename, ResourceType.DRL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    protected void cleanKnowledgeBase() {
        Collection<KnowledgePackage> kpackages = kbase.getKnowledgePackages();
        for (KnowledgePackage kp : kpackages) kbase.removeKnowledgePackage(kp.getName());
        Iterator<String> iter = listRulesFiles.iterator();
        while (iter.hasNext()) backupFile(iter.next());
        listRulesFiles.removeAll(listRulesFiles);
    }

    protected String getFullPathName(String file) {
        URL url = getClass().getResource(file);
        String fullname = new String();
        try {
            URI uri = new URI(url.toString());
            fullname = uri.getPath();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
            fullname = getClass().getResource(file).getPath();
            fullname.replaceAll("[%20]", " ");
        }
        return fullname;
    }

    protected void backupFile(String file) {
        Format formatter = new SimpleDateFormat("yyyyMMddhhmmss", Locale.ENGLISH);
        String newfile = getFullPathName(file) + "_" + formatter.format(new Date());
        logger.info("Making a backup file of " + file + " to " + newfile);
        String content = readFile(file);
        File outputFile = new File(newfile);
        try {
            FileWriter out = new FileWriter(outputFile);
            out.write(content);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String Policies2RulesFile(Policy[] policies) {
        String rulesString = "package org.slasoi.gslam.pac;" + "\n\n";
        for (Policy policy : policies) {
            int firstIndexOfClosedQuote = policy.getRule().indexOf("\"", policy.getRule().indexOf("\"") + 1);
            String ruleString = policy.getRule().substring(0, firstIndexOfClosedQuote + 1);
            if (policy.getId() + "".length() > 0) ruleString += ("\n@id(" + policy.getId() + ")");
            if (policy.getName() != null) ruleString += ("\n@name(" + policy.getName() + ")");
            if (policy.getRule() != null) ruleString += ("\n@rulebody(" + policy.getRule() + ")");
            if (policy.getContext() != null) {
                ruleString += ("\n@ContextId(" + policy.getContext().getId() + ")");
                ruleString += ("\n@ContextName(\"" + policy.getContext().getName() + "\"" + ")");
                ruleString += ("\n@ContextContent(\"" + policy.getContext().getContent() + "\"" + ")");
            }
            ruleString += policy.getRule().substring(firstIndexOfClosedQuote + 1);
            rulesString += (ruleString + "\n\n");
        }
        return rulesString;
    }
}
