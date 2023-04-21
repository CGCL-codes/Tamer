package org.didicero.base.tools;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator.RequestorType;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import org.didicero.base.entity.Answer;
import org.didicero.base.entity.BasalText;
import org.didicero.base.entity.Content;
import org.didicero.base.entity.Header;
import org.didicero.base.entity.Language;
import org.didicero.base.entity.LearningConsolidation;
import org.didicero.base.entity.LearningModule;
import org.didicero.base.entity.LearningModuleDaoException;
import org.didicero.base.entity.LearningPage;
import org.didicero.base.entity.LearningUnit;
import org.didicero.base.entity.Question;
import org.didicero.base.entity.Request;
import org.didicero.base.types.LearningMode;
import org.didicero.base.types.QuestMode;
import org.didicero.base.xml.freemind.Html;
import org.didicero.base.xml.freemind.Richcontent;
import org.didicero.base.xml.freemind.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FreemindHandler implements TempIdCreator {

    HTMLTextParser htmlparser;

    LearningModule lm = null;

    ModuleListener listener = null;

    int startlevel = 0;

    boolean asUnit = false;

    int tempID = 0;

    int pageCount = 0;

    int consCount = 0;

    MediaHandler mh = null;

    public FreemindHandler() {
        super();
        this.mh = new MediaHandler(this);
        htmlparser = new HTMLTextParser();
    }

    public FreemindHandler(ModuleListener listener) {
        super();
        this.mh = new MediaHandler(this);
        this.listener = listener;
    }

    public Long getTempID() {
        return new Long(--this.tempID);
    }

    public ModuleListener getListener() {
        return listener;
    }

    public void setListener(ModuleListener listener) {
        this.listener = listener;
    }

    public LearningModule getLm() {
        return lm;
    }

    public boolean isAsUnit() {
        return asUnit;
    }

    public void setAsUnit(boolean asUnit) {
        this.asUnit = asUnit;
    }

    public <T> T unmarshal(Class<T> docClass, InputStream inputStream) throws JAXBException {
        String packageName = docClass.getPackage().getName();
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller u = jc.createUnmarshaller();
        T doc = (T) u.unmarshal(inputStream);
        return doc;
    }

    public void parseNode(org.didicero.base.xml.freemind.Node node) throws Exception {
        this.clearTextBuffer();
        this.processNode(node, this.startlevel, null, null, null, null);
    }

    private void clearTextBuffer() {
        this.htmlparser.getTextBuffer().delete(0, this.htmlparser.getTextBuffer().length());
    }

    private Header createHeader(String title) throws Exception {
        Header hd = new Header(title, Language.GERMAN, true);
        if (this.listener != null) {
            this.listener.onHeaderCreate(hd);
        }
        if (hd.getId() == null) {
            hd.setId(this.getTempID());
        }
        return hd;
    }

    private LearningModule createModule(String title) throws Exception {
        Date now = new Date();
        this.lm = new LearningModule();
        this.lm.setCreatedate(now);
        this.lm.setChangedate(now);
        this.lm.setChangeuser("System");
        this.lm.setCreateuser("System");
        this.lm.setModuleTitle(this.createHeader(title));
        if (this.listener != null) {
            this.listener.onModuleCreate(lm);
        }
        if (this.lm.getId() == null) {
            this.lm.setId(this.getTempID());
        }
        return this.lm;
    }

    private LearningUnit createUnit(String title) throws Exception {
        LearningUnit lu = new LearningUnit();
        lu.setChangedate(this.lm.getChangedate());
        lu.setCreatedate(this.lm.getCreatedate());
        lu.setChangeuser(this.lm.getChangeuser());
        lu.setCreateuser(this.lm.getCreateuser());
        lu.setUnitTitle(this.createHeader(title));
        if (this.listener != null) {
            this.listener.onUnitCreate(lu);
        }
        if (lu.getId() == null) {
            lu.setId(this.getTempID());
        }
        return lu;
    }

    private Request createRequest(LearningPage page) throws Exception {
        Request rq = new Request();
        rq.setChangedate(this.lm.getChangedate());
        rq.setCreatedate(this.lm.getCreatedate());
        rq.setChangeuser(this.lm.getChangeuser());
        rq.setCreateuser(this.lm.getCreateuser());
        if (this.listener != null) {
            this.listener.onRequestCreate(rq);
        }
        if (rq.getId() == null) {
            rq.setId(this.getTempID());
        }
        return rq;
    }

    private Question createQuestion(Request rq, String text) throws Exception {
        Question lq = new Question(text, Language.GERMAN, true, 0, false, false, QuestMode.CLOZE);
        if (rq != null) {
            rq.getQuestions().add(lq);
        }
        if (this.listener != null) {
            this.listener.onQuestionCreate(lq);
        }
        if (lq.getId() == null) {
            lq.setId(this.getTempID());
        }
        return lq;
    }

    private Answer createAnswer(Question lq, String text) throws Exception {
        Answer answer = new Answer(text, Language.GERMAN, true, true, lq.getMyAnswers().size() + 1);
        if (this.listener != null) {
            this.listener.onAnswerCreate(answer);
        }
        if (answer.getId() == null) {
            answer.setId(this.getTempID());
        }
        return answer;
    }

    private LearningPage createPage(String title) throws Exception {
        LearningPage lp = new LearningPage();
        this.consCount = 0;
        lp.setMode(LearningMode.NORMAL);
        lp.setPagenr(++pageCount);
        lp.setPageTitle(this.createHeader(title));
        if (this.listener != null) {
            this.listener.onPageCreate(lp);
        }
        if (lp.getId() == null) {
            lp.setId(this.getTempID());
        }
        Request pageRequest = this.createRequest(lp);
        lp.setPageRequest(pageRequest);
        return lp;
    }

    private LearningConsolidation createConsolidation(LearningPage parent, String title) throws Exception {
        LearningConsolidation lc = new LearningConsolidation();
        lc.setMode(LearningMode.CONSOLIDATION);
        lc.setPagenr(++consCount);
        lc.setPageTitle(this.createHeader(title));
        if (this.listener != null) {
            this.listener.onConsolidationCreate(lc);
        }
        if (lc.getId() == null) {
            lc.setId(this.getTempID());
        }
        Request pageRequest = this.createRequest(lc);
        return lc;
    }

    private BasalText createBasalText(String text) throws Exception {
        BasalText basal = new BasalText();
        basal.setChangedate(this.lm.getChangedate());
        basal.setCreatedate(this.lm.getCreatedate());
        basal.setChangeuser(this.lm.getChangeuser());
        basal.setCreateuser(this.lm.getCreateuser());
        Content pureText = new Content(text, Language.GERMAN, true);
        basal.setPureText(pureText);
        if (this.listener != null) {
            this.listener.onTextCreate(basal);
        }
        if (basal.getId() == null) {
            basal.setId(this.getTempID());
        }
        return basal;
    }

    private String processNode(org.didicero.base.xml.freemind.Node node, int lvl, LearningUnit unit, LearningPage page, LearningConsolidation cons, Question quest) throws Exception {
        if (node == null) {
            return "";
        } else {
            int myLevel = lvl;
            LearningUnit myUnit = unit;
            LearningPage myPage = page;
            LearningConsolidation myCons = cons;
            Question myQuest = quest;
            Question prevQuest = quest;
            String nname = node.getID();
            Richcontent rc;
            Text tx;
            List childList = node.getAttributeOrArrowlinkOrCloud();
            int childLen = childList.size();
            String nodeText = "";
            StringBuffer nodeContent = new StringBuffer();
            if (node.getTEXT() != null) {
                nodeText = node.getTEXT();
                if (nodeText.length() > 0) {
                    while (((nodeText.charAt(0) == '\n') || (nodeText.charAt(0) == '\r') || (nodeText.charAt(0) == ' ')) && (nodeText.length() > 0)) {
                        nodeText = nodeText.substring(1, nodeText.length());
                    }
                    while (((nodeText.charAt(0) == '\n') || (nodeText.charAt(0) == '\r') || (nodeText.charAt(0) == ' ')) && (nodeText.length() > 0)) {
                        nodeText = nodeText.substring(0, nodeText.length() - 1);
                    }
                }
                if (myLevel == 0) {
                    this.createModule(nodeText);
                }
                if (myLevel == 1) {
                    myUnit = this.createUnit(nodeText);
                    if (this.lm.getLearningUnits().add(myUnit)) {
                        System.out.println("Unit: " + this.lm.getLearningUnits().size());
                    }
                }
                if (myLevel == 1) {
                    myPage = this.createPage(nodeText);
                    myUnit.getPages().add(myPage);
                }
                if (myLevel == 2) {
                    myCons = this.createConsolidation(myPage, nodeText);
                    myUnit.getDetailPages().add(myCons);
                }
                if ((myLevel >= 1) && (prevQuest != null)) {
                    this.createAnswer(prevQuest, nodeText);
                }
                if (myLevel >= 1) {
                    if ((myLevel == 1) && (myPage != null)) {
                        myQuest = this.createQuestion(myPage.getPageRequest(), nodeText);
                    }
                    if ((myLevel > 1) && (myCons != null)) {
                        myQuest = this.createQuestion(myCons.getPageRequest(), nodeText);
                    }
                }
            }
            ;
            if (childLen > 0) {
                org.didicero.base.xml.freemind.Node procNode = null;
                for (Iterator it = childList.iterator(); it.hasNext(); ) {
                    Object object = (Object) it.next();
                    if (object instanceof org.didicero.base.xml.freemind.Text) {
                        tx = (Text) object;
                    } else if (object instanceof org.didicero.base.xml.freemind.Richcontent) {
                        rc = (Richcontent) object;
                        Html html = rc.getHtml();
                        if (html != null) {
                            List any = html.getAny();
                            for (Iterator itNode = any.iterator(); itNode.hasNext(); ) {
                                Object oNode = (Object) itNode.next();
                                if (oNode instanceof org.w3c.dom.Node) {
                                    org.w3c.dom.Node domNode = (org.w3c.dom.Node) oNode;
                                    if (domNode.getNodeName().equalsIgnoreCase("body")) {
                                        this.clearTextBuffer();
                                        this.htmlparser.processNode(domNode);
                                        StringBuffer buf = this.htmlparser.getTextBuffer();
                                        if (this.htmlparser.getImageURIMap().size() > 0) {
                                            Collection urls = this.htmlparser.getImageURIMap().values();
                                            for (Iterator itUrls = urls.iterator(); itUrls.hasNext(); ) {
                                                String url = (String) itUrls.next();
                                                this.mh.createImageFromUrl(url);
                                            }
                                        }
                                        nodeText = buf.toString();
                                        if (nodeText.length() > 0) {
                                            while (((nodeText.charAt(0) == '\n') || (nodeText.charAt(0) == '\r') || (nodeText.charAt(0) == ' ')) && (nodeText.length() > 0)) {
                                                nodeText = nodeText.substring(1, nodeText.length());
                                            }
                                            while (((nodeText.charAt(0) == '\n') || (nodeText.charAt(0) == '\r') || (nodeText.charAt(0) == ' ')) && (nodeText.length() > 0)) {
                                                nodeText = nodeText.substring(0, nodeText.length() - 1);
                                            }
                                        }
                                        if ((myLevel >= 1) && (prevQuest != null)) {
                                            this.createAnswer(prevQuest, nodeText);
                                        }
                                        if (myLevel >= 1) {
                                            if ((myLevel == 1) && (myPage != null)) {
                                                myQuest = this.createQuestion(myPage.getPageRequest(), nodeText);
                                            }
                                            if ((myLevel > 1) && (myCons != null)) {
                                                myQuest = this.createQuestion(myCons.getPageRequest(), nodeText);
                                            }
                                        }
                                        if (myLevel == 0) {
                                            this.createModule(nodeText);
                                        }
                                        if (myLevel == 1) {
                                            myUnit = this.createUnit(nodeText);
                                            if (this.lm.getLearningUnits().size() > 0) {
                                                LearningUnit alt = this.lm.getLearningUnits().iterator().next();
                                                if (alt.equals(myUnit)) {
                                                    System.out.println("Mist");
                                                }
                                            }
                                            this.lm.getLearningUnits().add(myUnit);
                                        }
                                        if (myLevel == 1) {
                                            myPage = this.createPage(nodeText);
                                            myUnit.getPages().add(myPage);
                                        }
                                        if (myLevel == 2) {
                                            myCons = this.createConsolidation(myPage, nodeText);
                                            myUnit.getDetailPages().add(myCons);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for (Iterator it = childList.iterator(); it.hasNext(); ) {
                    Object object = (Object) it.next();
                    if (object instanceof org.didicero.base.xml.freemind.Node) {
                        procNode = (org.didicero.base.xml.freemind.Node) object;
                        if ((myUnit == null) && (myLevel > 0)) {
                            String stop = " !!!!!!!!!!!!!!!!";
                            System.out.println("ErrorNode at " + nname + " " + stop);
                        } else {
                            String subText = processNode(procNode, myLevel + 1, myUnit, myPage, myCons, myQuest);
                            if ((myLevel >= 1)) {
                                if (nodeContent.length() > 0) {
                                    nodeContent.append("\n - ");
                                }
                                nodeContent.append(subText);
                                nodeText = nodeContent.toString();
                                BasalText basal;
                                if ((myLevel == 1) && (myPage != null)) {
                                    basal = createBasalText(nodeText);
                                    basal.setOrderNr(myPage.getLearningTexts().size() + 1);
                                    myPage.getLearningTexts().add(basal);
                                }
                                if ((myLevel > 1) && (myCons != null)) {
                                    basal = createBasalText(nodeText);
                                    basal.setOrderNr(myCons.getLearningTexts().size() + 1);
                                    myCons.getLearningTexts().add(basal);
                                }
                                System.out.println("Content lenght at level <" + myLevel + "> is " + nodeContent.length());
                            }
                        }
                    }
                }
            }
            return nodeText;
        }
    }
}
