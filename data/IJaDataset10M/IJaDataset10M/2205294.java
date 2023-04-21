package org.authorsite.bib.web.struts.action;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.authorsite.bib.dto.*;
import org.authorsite.bib.ejb.facade.*;
import org.authorsite.bib.web.struts.form.*;
import org.authorsite.bib.web.struts.util.*;
import org.authorsite.bib.web.util.EJBHomeFactory;

/**
 * @author  jejking
 * @version $Revision: 1.3 $
 */
public class MediaItemManagementAddProdRelsAction extends BibAbstractAction {

    private final int PEOPLE = 1;

    private final int ORGS = 2;

    /** Creates a new instance of MediaItemManagementAddProdRelsAction */
    public MediaItemManagementAddProdRelsAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int prodRelPK = 0;
        MediaProductionRelationshipDTO mprDTO = null;
        String[] personID = ((ProdRelsForm) form).getPersonID();
        String[] orgID = ((ProdRelsForm) form).getOrgID();
        String productionRelationship = ((ProdRelsForm) form).getProductionRelationship();
        boolean finishedFlag = ((ProdRelsForm) form).getFinishedFlag();
        System.out.println("obtained variables from form");
        EJBHomeFactory ejbHomeFactory = EJBHomeFactory.getInstance();
        MediaItemManagementFacadeHome home = (MediaItemManagementFacadeHome) ejbHomeFactory.lookupHome("ejb/MediaItemManagementFacadeEJB", MediaItemManagementFacadeHome.class);
        MediaItemManagementFacade facade = home.create();
        System.out.println("obtained ejb ref");
        HttpSession session = request.getSession();
        MediaItemDTO activeMediaItem = (MediaItemDTO) session.getAttribute("ActiveMediaItem");
        System.out.println("got active media item");
        Set activeMediaItemProdRels = activeMediaItem.getMediaProductionRelationships();
        if (activeMediaItemProdRels == null) {
            activeMediaItemProdRels = new HashSet();
        }
        if (activeMediaItemProdRels.size() == 0) {
            System.out.println("there are no production relationships for the active media item dto");
            prodRelPK = createNewProductionRelationship(facade, activeMediaItem.getID().intValue(), productionRelationship);
            mprDTO = new MediaProductionRelationshipDTO(new Integer(prodRelPK));
            mprDTO.setRelationshipType(productionRelationship);
            System.out.println("created new production relationship, " + prodRelPK);
        } else {
            boolean relationshipExists = false;
            System.out.println("this media item dto does apparently have some production relationships");
            Iterator it = activeMediaItemProdRels.iterator();
            while (it.hasNext()) {
                mprDTO = (MediaProductionRelationshipDTO) it.next();
                if (productionRelationship.equals(mprDTO.getRelationshipType())) {
                    relationshipExists = true;
                    System.out.println("This media item dto has the production relationship submitted in the form");
                    break;
                }
            }
            if (!relationshipExists) {
                System.out.println("Although there are some prod rels, there isnt this one. Making it");
                prodRelPK = createNewProductionRelationship(facade, activeMediaItem.getID().intValue(), productionRelationship);
                mprDTO = new MediaProductionRelationshipDTO(new Integer(prodRelPK));
                mprDTO.setRelationshipType(productionRelationship);
                System.out.println("Created new production relationship," + prodRelPK);
            } else {
                prodRelPK = mprDTO.getID().intValue();
                System.out.println("We found the prod rel already existing. Its PK is " + prodRelPK);
            }
        }
        if (personID.length > 0) {
            System.out.println("about to call updateEJBWithPeople");
            updateEJBWithPeople(facade, prodRelPK, removeDuplicates(personID, mprDTO.getPeople()));
            System.out.println("about to call updateActiveMediaItemWithPeople");
            updateActiveMediaItemWithPeople(mprDTO, personID, session);
        }
        if (orgID.length > 0) {
            System.out.println("about to call updateEJBWithOrgs");
            updateEJBWithOrgs(facade, prodRelPK, removeDuplicates(orgID, mprDTO.getOrganisations()));
            System.out.println("About to call updateActiveMEdiaItemWithORgs");
            updateActiveMediaItemWithOrgs(mprDTO, orgID, session);
        }
        System.out.println("about to add MediaItemProductionRelationshipDTO to activeMediaItemProdRels");
        activeMediaItemProdRels.add(mprDTO);
        System.out.println("about to add activeMediaItemProdRels to active media item");
        activeMediaItem.setMediaProductionRelationships(activeMediaItemProdRels);
        if (finishedFlag) {
            return mapping.findForward("addIMRs");
        } else {
            return mapping.findForward("addMoreProdRels");
        }
    }

    private int createNewProductionRelationship(MediaItemManagementFacade facade, int activeMediaItemPK, String productionRelationship) throws Exception {
        return facade.addMediaProductionRelationship(activeMediaItemPK, productionRelationship);
    }

    private void updateEJBWithPeople(MediaItemManagementFacade facade, int prodRelPK, String[] personID) throws Exception {
        for (int i = 0; i < personID.length; i++) {
            facade.addPersonToProductionRelationship(prodRelPK, Integer.parseInt(personID[i]));
        }
    }

    private void updateEJBWithOrgs(MediaItemManagementFacade facade, int prodRelPK, String[] orgID) throws Exception {
        for (int i = 0; i < orgID.length; i++) {
            facade.addOrganisationToProductionRelationship(prodRelPK, Integer.parseInt(orgID[i]));
        }
    }

    private void updateActiveMediaItemWithPeople(MediaProductionRelationshipDTO dto, String[] personID, HttpSession session) {
        HashMap peopleClipboardMap = (HashMap) session.getAttribute("PeopleClipboardMap");
        if (peopleClipboardMap == null) {
            return;
        }
        Set dtoPeople = dto.getPeople();
        for (int i = 0; i < personID.length; i++) {
            PersonDTO personDTO = (PersonDTO) peopleClipboardMap.get(new Integer(personID[i]));
            dtoPeople.add(personDTO);
            System.out.println("added personDTO to active media item: " + personDTO.getMainName());
        }
    }

    private void updateActiveMediaItemWithOrgs(MediaProductionRelationshipDTO dto, String[] orgID, HttpSession session) {
        HashMap orgsClipboardMap = (HashMap) session.getAttribute("OrgsClipboardMap");
        if (orgsClipboardMap == null) {
            return;
        }
        Set dtoOrgs = dto.getOrganisations();
        for (int i = 0; i < orgID.length; i++) {
            OrganisationDTO orgDTO = (OrganisationDTO) orgsClipboardMap.get(new Integer(orgID[i]));
            dtoOrgs.add(orgDTO);
        }
    }

    private String[] removeDuplicates(String[] array, Set existing) {
        HashSet noDuplicates = new HashSet(Arrays.asList(array));
        Iterator existingIt = existing.iterator();
        while (existingIt.hasNext()) {
            AbstractDTO dto = (AbstractDTO) existingIt.next();
            if (noDuplicates.contains(dto.getID().toString())) {
                noDuplicates.remove(dto.getID().toString());
            }
        }
        System.out.println("removed duplicates");
        return (String[]) noDuplicates.toArray(new String[noDuplicates.size()]);
    }
}
