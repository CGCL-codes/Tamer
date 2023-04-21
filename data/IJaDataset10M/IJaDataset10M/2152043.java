package si.unimb.isportal07.iiDoc.controller.docStates;

import si.unimb.isportal07.iiDoc.controller.DocController;
import si.unimb.isportal07.iiDoc.dbobj.Comments;
import si.unimb.isportal07.iiDoc.dbobj.Docs;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.Input;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.dbobj.ValidValue;
import com.jcorporate.expresso.core.security.User;
import java.util.Vector;
import si.unimb.isportal07.iiIskalnik.iskanje.Vnasalec;
import si.unimb.isportal07.iiCategories.*;

public class DetailDocsState extends State {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 1L;

    public static final String STATE_NAME = "detailDocsState";

    /**
	 * Constructor
	 */
    public DetailDocsState() {
        super(DetailDocsState.STATE_NAME, DetailDocsState.STATE_NAME + "Description");
    }

    @SuppressWarnings("unchecked")
    private boolean preveriUporabnika() {
        String value = "Admin";
        try {
            User test = User.getUser(this.getControllerRequest());
            Vector member = test.getGroups();
            for (int i = 0; i < member.size(); i++) {
                if (value.compareTo((String) member.get(i)) == 0) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    @SuppressWarnings("unchecked")
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
            Output uporabnik = new Output();
            uporabnik.setName("uporabnik");
            if (preveriUporabnika() == true) {
                uporabnik.setContent("True");
            } else {
                uporabnik.setContent("False");
            }
            response.add(uporabnik);
            Transition errorTrans = new Transition();
            errorTrans.setControllerObject(DocController.class);
            errorTrans.setState(ErrorDocsState.STATE_NAME);
            errorTrans.setName("errorTrans");
            setErrorTransition(errorTrans);
            Transition errorTrans_advanced = new Transition();
            errorTrans_advanced.setControllerObject(DocController.class);
            errorTrans_advanced.setState(ErrorDocsState.STATE_NAME + "&style=errorDocsState_advanced");
            errorTrans_advanced.setName("errorTrans_advanced");
            setErrorTransition(errorTrans_advanced);
            Docs docDB = new Docs();
            docDB.setField(Docs.ID, request.getParameter("DocId"));
            docDB.retrieve();
            Comments commentsDB = new Comments();
            commentsDB.setField(Comments.ID, request.getParameter("IDdok"));
            if (request.getParameter("stanjeIn") != null) {
                docDB.setField(Docs.ID, request.getParameter(Docs.ID));
                docDB.retrieve();
                String staroStanje = docDB.getField(Docs.STATE);
                String novoStanje = request.getParameter("stanjeIn");
                if (staroStanje.equals("1") && novoStanje.equals("0")) {
                    String ajdi = docDB.getField(Docs.ID);
                    String odstrani_url = "Doc.do?DocId=" + ajdi + "&state=detailDocsState";
                    System.out.println("ONEMOGOCAM:Odstranjujem:" + odstrani_url);
                    Vnasalec.brisi(odstrani_url);
                }
                if (staroStanje.equals("0") && novoStanje.equals("1")) {
                    String ajdi = docDB.getField(Docs.ID);
                    String dodaj_url = "Doc.do?DocId=" + ajdi + "&state=detailDocsState";
                    System.out.println("OMOGOCAM:Dodajam:" + dodaj_url);
                    Vnasalec.indeksiraj(dodaj_url, "Dokument", docDB.getField(Docs.TITLE), docDB.getField(Docs.KEY_WORDS));
                }
                docDB.setField(Docs.STATE, novoStanje);
                docDB.update(true);
            }
            docDB.setField(Docs.ID, request.getParameter(Docs.ID));
            docDB.retrieve();
            Output imeOut = new Output("imeOut", docDB.getField(Docs.TITLE));
            imeOut.setLabel(getString("ime"));
            response.add(imeOut);
            Output idOut = new Output();
            idOut.setName("idOut");
            idOut.setContent(docDB.getField(Docs.ID));
            response.add(idOut);
            Output ocenaOut = new Output("ocenaOut", docDB.getField(Docs.VALUE));
            ocenaOut.setLabel(getString("ocena"));
            response.add(ocenaOut);
            Output velikostOut = new Output("velikostOut", docDB.getField("file_fileSize"));
            velikostOut.setLabel(getString("velikost"));
            response.add(velikostOut);
            Output vrstaOut = new Output("vrstaOut", docDB.getField("file_mimeType"));
            vrstaOut.setLabel(getString("vrsta"));
            response.add(vrstaOut);
            String t = docDB.getField(Docs.CATEGORY_ID);
            Vector temp = CategoriesFasade.getAll();
            String izhod = "";
            for (int i = 0; i < temp.size(); i++) {
                ValidValue vv = (ValidValue) temp.get(i);
                if (vv.getValue().equals(t)) {
                    izhod = vv.getDescription();
                }
            }
            Output idKatOut = new Output("idKatOut", izhod);
            idKatOut.setLabel(getString("idkategorija"));
            response.add(idKatOut);
            Output datumOut = new Output("datumOut", docDB.getField(Docs.DATE_CREATED));
            datumOut.setLabel(getString("datum"));
            response.add(datumOut);
            Output opisOut = new Output("opisOut", docDB.getField(Docs.DESCRIPTION));
            opisOut.setLabel(getString("opis3"));
            response.add(opisOut);
            t = docDB.getField(Docs.LANGUAGE);
            temp = (docDB.getValidValues(Docs.LANGUAGE));
            izhod = "";
            for (int i = 0; i < temp.size(); i++) {
                ValidValue vv = (ValidValue) temp.get(i);
                if (vv.getValue().equals(t)) {
                    izhod = vv.getDescription();
                }
            }
            Output jezikOut = new Output("jezikOut", izhod);
            jezikOut.setLabel(getString("jezik2"));
            response.add(jezikOut);
            String stanje_temp = docDB.getField(Docs.STATE);
            String vun = getString("one");
            if (stanje_temp.equals("-1") || stanje_temp.equals("1")) vun = getString("omo"); else if (stanje_temp.equals("-2") || stanje_temp.equals("2")) vun = getString("pre");
            Output stanjeOut = new Output("stanjeOut", vun);
            stanjeOut.setLabel(getString("stanje"));
            response.add(stanjeOut);
            Input stanjeIn = new Input();
            stanjeIn.setName("stanjeIn");
            stanjeIn.setType(Input.ATTRIBUTE_DROPDOWN);
            stanjeIn.setLabel(getString("stanje2"));
            stanjeIn.setValidValues(docDB.getValidValues(Docs.STATE));
            response.add(stanjeIn);
            Input brisiIn = new Input();
            brisiIn.setName("brisiIn");
            brisiIn.setType(Input.ATTRIBUTE_CHECKBOX);
            brisiIn.setLabel(getString("brisi"));
            response.add(brisiIn);
            Input ocenaIn = new Input();
            ocenaIn.setName("ocenaIn");
            ocenaIn.setType(Input.ATTRIBUTE_DROPDOWN);
            ocenaIn.setLabel(getString("ocena2"));
            ocenaIn.setValidValues(docDB.getValidValues(Docs.VALUE));
            response.add(ocenaIn);
            Output commentOut = new Output("commentOut", docDB.getField(Comments.COMMENT));
            commentOut.setLabel(getString("komentar2"));
            response.add(commentOut);
            Transition seznamDokumentovTransition = new Transition();
            seznamDokumentovTransition.setControllerObject(DocController.class);
            seznamDokumentovTransition.setState(ListDocsState.STATE_NAME);
            seznamDokumentovTransition.setName("seznamDokumentovTransition");
            seznamDokumentovTransition.setLabel(getString("seznamDokumentovTransition"));
            response.add(seznamDokumentovTransition);
            Transition seznamDokumentovTransition_advanced = new Transition();
            seznamDokumentovTransition_advanced.setControllerObject(DocController.class);
            seznamDokumentovTransition_advanced.setState(ListDocsState.STATE_NAME + "&style=listDocsState_advanced");
            seznamDokumentovTransition_advanced.setName("seznamDokumentovTransition_advanced");
            seznamDokumentovTransition_advanced.setLabel(getString("seznamDokumentovTransition"));
            response.add(seznamDokumentovTransition_advanced);
            Transition oceniDokumentTransition = new Transition();
            oceniDokumentTransition.setControllerObject(DocController.class);
            oceniDokumentTransition.setState(AddValueDocsState.STATE_NAME);
            oceniDokumentTransition.setName("oceniDokumentTransition");
            oceniDokumentTransition.addParam(Docs.ID, getParameter(Docs.ID));
            oceniDokumentTransition.setLabel(getString("oceniDokumentTransition"));
            response.add(oceniDokumentTransition);
            Transition oceniDokumentTransition_advanced = new Transition();
            oceniDokumentTransition_advanced.setControllerObject(DocController.class);
            oceniDokumentTransition_advanced.setState(AddValueDocsState.STATE_NAME + "&style=addValueDocsState_advanced");
            oceniDokumentTransition_advanced.setName("oceniDokumentTransition_advanced");
            oceniDokumentTransition_advanced.addParam(Docs.ID, getParameter(Docs.ID));
            oceniDokumentTransition_advanced.setLabel(getString("oceniDokumentTransition"));
            response.add(oceniDokumentTransition_advanced);
            Transition potrdiTransition = new Transition();
            potrdiTransition.setControllerObject(DocController.class);
            potrdiTransition.setState(DetailDocsState.STATE_NAME);
            potrdiTransition.setName("potrdiTransition");
            potrdiTransition.addParam(Docs.ID, getParameter(Docs.ID));
            potrdiTransition.setLabel(getString("potrdiTransition"));
            response.add(potrdiTransition);
            Transition potrdiTransition_advanced = new Transition();
            potrdiTransition_advanced.setControllerObject(DocController.class);
            potrdiTransition_advanced.setState(DetailDocsState.STATE_NAME + "&style=detailDocsState_advanced");
            potrdiTransition_advanced.setName("potrdiTransition_advanced");
            potrdiTransition_advanced.addParam(Docs.ID, getParameter(Docs.ID));
            potrdiTransition_advanced.setLabel(getString("potrdiTransition"));
            response.add(potrdiTransition_advanced);
            Transition downloadTransition = new Transition();
            downloadTransition.setControllerObject(DocController.class);
            downloadTransition.setState(ListDocsState.STATE_NAME);
            downloadTransition.setName("downloadTransition");
            downloadTransition.setLabel(getString("downloadTransition"));
            response.add(downloadTransition);
            Transition downloadTransition_advanced = new Transition();
            downloadTransition_advanced.setControllerObject(DocController.class);
            downloadTransition_advanced.setState(ListDocsState.STATE_NAME + "&style=listDocsState_advanced");
            downloadTransition_advanced.setName("downloadTransition_advanced");
            downloadTransition_advanced.setLabel(getString("downloadTransition"));
            response.add(downloadTransition_advanced);
            Transition brisiTransition = new Transition();
            brisiTransition.setControllerObject(DocController.class);
            brisiTransition.setState(WantToDeleteDocsState.STATE_NAME);
            brisiTransition.setName("brisiTransition");
            brisiTransition.setLabel(getString("brisiTransition"));
            brisiTransition.addParam(Docs.ID, getParameter(Docs.ID));
            response.add(brisiTransition);
            Transition brisiTransition_advanced = new Transition();
            brisiTransition_advanced.setControllerObject(DocController.class);
            brisiTransition_advanced.setState(WantToDeleteDocsState.STATE_NAME + "&style=wantToDeleteDocsState_advanced");
            brisiTransition_advanced.setName("brisiTransition_advanced");
            brisiTransition_advanced.setLabel(getString("brisiTransition"));
            brisiTransition_advanced.addParam(Docs.ID, getParameter(Docs.ID));
            response.add(brisiTransition_advanced);
            Transition popraviTransition = new Transition();
            popraviTransition.setControllerObject(DocController.class);
            popraviTransition.setState(PromptEditDocsState.STATE_NAME);
            popraviTransition.setName("popraviTransition");
            popraviTransition.setLabel(getString("popraviTransition"));
            popraviTransition.addParam(Docs.ID, getParameter(Docs.ID));
            response.add(popraviTransition);
            Transition popraviTransition_advanced = new Transition();
            popraviTransition_advanced.setControllerObject(DocController.class);
            popraviTransition_advanced.setState(PromptEditDocsState.STATE_NAME + "&style=promptEditDocsState_advanced");
            popraviTransition_advanced.setName("popraviTransition_advanced");
            popraviTransition_advanced.setLabel(getString("popraviTransition"));
            popraviTransition_advanced.addParam(Docs.ID, getParameter(Docs.ID));
            response.add(popraviTransition_advanced);
            Transition submitNewCommentTrans = new Transition();
            submitNewCommentTrans.setName("submitNewCommentTrans");
            submitNewCommentTrans.setLabel(getString("submitNewCommentTrans"));
            submitNewCommentTrans.setControllerObject(DocController.class);
            submitNewCommentTrans.addParam(Docs.ID, request.getParameter("DocId"));
            submitNewCommentTrans.setState(PromptAddCommentState.STATE_NAME);
            response.add(submitNewCommentTrans);
            Transition submitNewCommentTrans_advanced = new Transition();
            submitNewCommentTrans_advanced.setName("submitNewCommentTrans_advanced");
            submitNewCommentTrans_advanced.setLabel(getString("submitNewCommentTrans"));
            submitNewCommentTrans_advanced.setControllerObject(DocController.class);
            submitNewCommentTrans_advanced.addParam(Docs.ID, request.getParameter("DocId"));
            submitNewCommentTrans_advanced.setState(PromptAddCommentState.STATE_NAME + "&style=promptAddCommentState_advanced");
            response.add(submitNewCommentTrans_advanced);
            Transition listCommentsTransition = new Transition();
            listCommentsTransition.setControllerObject(DocController.class);
            listCommentsTransition.setState(ListCommentState.STATE_NAME);
            listCommentsTransition.setName("listCommentsTransition");
            listCommentsTransition.setLabel(getString("listCommentsTransition"));
            listCommentsTransition.addParam("DocId", request.getParameter(Docs.ID));
            response.add(listCommentsTransition);
            Transition listCommentsTransition_advanced = new Transition();
            listCommentsTransition_advanced.setControllerObject(DocController.class);
            listCommentsTransition_advanced.setState(ListCommentState.STATE_NAME + "&style=listCommentState_advanced");
            listCommentsTransition_advanced.setName("listCommentsTransition_advanced");
            listCommentsTransition_advanced.setLabel(getString("listCommentsTransition"));
            listCommentsTransition_advanced.addParam("DocId", request.getParameter(Docs.ID));
            response.add(listCommentsTransition_advanced);
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
        }
    }
}
