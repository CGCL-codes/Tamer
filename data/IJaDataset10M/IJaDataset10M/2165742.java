/**
 * The  "epoline (r) PHOENIX (ePHOENIX) software" which you may download is a 
 * software which is a document and dossier management system developed to 
 * support the process of handling Intellectual Property-related documents and 
 * hereinafter is referred to as "the Program".
 *
 * Copyright (c) 2005 European Patent Organisation
 *
 * The European Patent Organisation has made available the source code to pave 
 * the way for the system to become a world standard for Intellectual Property 
 * document archiving and to facilitate document exchange between offices, 
 * which will reduce costs for applicants and increase efficiencies within the
 * offices.
 *
 * To accomplish this goal a Release Board has been formed to ensure the 
 * maximum possible international uniformity in this area. The members of this
 * Release Board are various governmental and intergovernmental 
 * administrations: EPO, USPTO, JPO, WIPO and several other interested 
 * organisations. For details about the Release Board and how to become a 
 * member thereof see http://ephx.sourceforge.net .
 *
 * You are requested to provide your modifications and contributions on the 
 * Program to the ePHOENIX Open Source community on 
 * http://ephx.sourceforge.net, so as to make it possible for the Release 
 * Board to assess them and incorporate them into a new certified release of 
 * the Program.
 *
 * You can redistribute this Program and/or modify it in accordance with the 
 * terms of the European Patent Organisation Open Source Licence No. 2005/1.
 *
 * This Program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the European Patent Organisation Open
 * Source Licence No. 2005/1 for more details.
 * 
 * You should have received a copy of the European Patent Organisation Open 
 * Source Licence No. 2005/1 along with this Program; if not write to 
 * epoline (r), European Patent Office - P.O. Box 5818 - 2280HV Rijswijk (ZH)
 * - Netherlands or email to the Administrator of the ePHOENIX project at 
 * SourceForge (see: http://sourceforge.net/project/ephx.
 *
 * If the program is interactive, make it output a short notice like this when
 * it starts in an interactive mode:
 * 
 * epoline (r) Phoenix, Copyright (c) 2005 European Patent Organisation, comes
 * with ABSOLUTELY NO WARRANTY; for details see the European Patent 
 * Organisation Open Source Licence No. 2005/1 at
 * http://ephx.sourceforge.net. You are welcome to redistribute it under
 * certain conditions; for details see the European Patent Organisation Open
 * Source Licence No. 2005/1 at http://ephx.sourceforge.net .
 */
package org.epoline.phoenix.printing;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.epoline.authentication.general.ADClient;
import org.epoline.legacy.general.DSS_ItemIdentifier;
import org.epoline.legacy.general.DSS_LegServerLink;
import org.epoline.legacy.general.LegacyException;
import org.epoline.legacy.general.LegacyServerItemInterface;
import org.epoline.legacy.support.DSS_LegServerFactory;
import org.epoline.legacy.support.SSLConfigManager;
import org.epoline.phoenix.authentication.shared.UserAuthenticator;
import org.epoline.phoenix.authentication.shared.UserProfile;
import org.epoline.phoenix.backend.DmsLinkException;
import org.epoline.phoenix.common.AbstractService;
import org.epoline.phoenix.common.UtilService;
import org.epoline.phoenix.common.backend.BackendUtils;
import org.epoline.phoenix.common.shared.ItemUser;
import org.epoline.phoenix.common.shared.MutableDataModel;
import org.epoline.phoenix.common.shared.PhoenixException;
import org.epoline.phoenix.common.shared.PhoenixObjectNotFoundException;
import org.epoline.phoenix.dms.DMSActionlog;
import org.epoline.phoenix.dms.DMSDocument;
import org.epoline.phoenix.dms.DMSDossier;
import org.epoline.phoenix.dms.DMSForm;
import org.epoline.phoenix.dms.DMSMember;
import org.epoline.phoenix.dms.DMSOverlay;
import org.epoline.phoenix.dms.DMSPaperfile;
import org.epoline.phoenix.dms.DMSTeam;
import org.epoline.phoenix.dms.DMSUser;
import org.epoline.phoenix.dossiernotepad.shared.ItemAction;
import org.epoline.phoenix.dossiernotepad.shared.ItemDocument;
import org.epoline.phoenix.printing.shared.IServicePrinting;
import org.epoline.phoenix.printing.shared.ItemPPFLine;
import org.epoline.print.shared.PaperfileAPubl;
import org.epoline.print.shared.PaperfileBPubl;
import org.epoline.print.shared.PaperfileCited;

//TODO why is there no authorization???

public class ServicePrinting extends AbstractService implements IServicePrinting {

	private BackendUtils	_dmsManager;
	private DMSOverlay 		_dateOverlay;
	private DMSOverlay 		_senderOverlay;

	private ServicePrinting() {
		super();
	}

	public void addActions(UserAuthenticator user, String dossierNr, List actions) throws PhoenixException, RemoteException {
		//checkAuthorization(user, PRINTING_LOCAL_FUNCTION);
		try {
			DMSDossier dmsDossier = getDmsManager().getDMSDossierByNumber(dossierNr);
			if (dmsDossier != null) {
				Iterator iter = actions.iterator();
				while (iter.hasNext()) {
					ItemAction action = (ItemAction) iter.next();
					DMSActionlog dmsActionlog = new DMSActionlog();
					dmsActionlog.setActDate(new java.sql.Date(System.currentTimeMillis()));
					dmsActionlog.setActPhxName(action.getName());
					dmsActionlog.setActAction(action.getDescription());
					dmsActionlog.setActUser(action.getUser());
					dmsDossier.addChild(dmsActionlog, false);
				}
				dmsDossier.updateTheDms(null);
			}
		}
		catch (DmsLinkException x) {
			throw new PhoenixException(x.toString());
		}
		catch (LegacyException x) {
			throw new PhoenixException(x.toString());
		}
	}

	public List getActionLog(UserAuthenticator user, String dossierNr) throws PhoenixException, RemoteException {
		//checkAuthorization(user, PRINTING_LOCAL_FUNCTION);
		ArrayList list = new ArrayList();
		try {
			DMSDossier dossier = getDmsManager().getDMSDossierByNumber(dossierNr);
			if (dossier != null) {
				Enumeration enum = dossier.getActionlogItems();
				while (enum.hasMoreElements()) {
					DMSActionlog dmsActionlog = (DMSActionlog) enum.nextElement();
					ItemAction action = new ItemAction(dmsActionlog.getActDate(), dmsActionlog.getActUser().trim(),
							dmsActionlog.getActPhxName().trim(), dmsActionlog.getActAction().trim());
					list.add(action);
				}
			}
		}
		catch (DmsLinkException x) {
			throw new PhoenixException(x.toString());
		}
		catch (LegacyException x) {
			throw new PhoenixException(x.toString());
		}
		return list;
	}

	public String getDocumentLanguage(UserAuthenticator user, String docKey) throws PhoenixException, RemoteException {
		try {
			if (docKey == null) {
				throw new IllegalArgumentException("doc key is null");
			}
			if (docKey.trim().equals("")) {
				throw new IllegalArgumentException("empty doc key");
			}
			DMSDocument dmsDocument = DMSDocument.getByKey(docKey);
			if (dmsDocument == null) {
				throw new PhoenixObjectNotFoundException("document with key: " + docKey);
			}
			String language = dmsDocument.getDocFrmLang();
			if (language == null) {
				language = "";
			}
			//return "NL";//for testing
			return language;
		}
		catch (DmsLinkException x) {
			throw new PhoenixException(x.toString());
		}
	}

	public MutableDataModel getDossiers(UserAuthenticator user) throws PhoenixException, RemoteException {
		//checkAuthorization(user, PRINTING_LOCAL_FUNCTION);
		return new MutableDataModel(new ArrayList());
	}

	public boolean[] getOverlays(UserAuthenticator user, ItemDocument document, String dossierNr)
			throws PhoenixException, RemoteException {
		//checkAuthorization(user, PRINTING_LOCAL_FUNCTION);
		boolean[] result = new boolean[2];

		UserProfile profile = getUserProfileForUserAuthenticator(user);
		String currentUser = profile.getUserId();
		String userPwd = profile.getPassword();

		//retrieve procedural language
		String prolLng = getPROL(dossierNr, currentUser, userPwd);
		if (prolLng == null) {
			prolLng = "EN";//default
		}

		DMSForm currentForm = null;
		//retreve the overlays from the appropriate form
		Iterator formIter = UtilService.getDmsCache().getDMSFormItems().iterator();
		while (formIter.hasNext()) {
			DMSForm form = (DMSForm) formIter.next();
			if (form.getFrmLang().equals("XX") || form.getFrmLang().equals(prolLng)) {
				currentForm = form;
				break;
			}
		}
		_dateOverlay = null;
		_senderOverlay = null;
		if (currentForm != null) {
			Iterator ovlIter = UtilService.getDmsCache().getDMSOverlayItems().iterator();
			while (ovlIter.hasNext()) {
				DMSOverlay overlay = (DMSOverlay) ovlIter.next();
				if (overlay.getOVLFRMKEY().equals(currentForm.getKey())) {
					switch (overlay.getOvlIndPageType()) {
					case 1:
						_dateOverlay = overlay;
						result[0] = true;
						break;
					case 3:
						_senderOverlay = overlay;
						result[1] = true;
						break;
					}
				}
			}
		}
		return result;
	}

	public List getPaperfileTypes(UserAuthenticator user, String teamUnit) throws PhoenixException, RemoteException {
		
		//checkAuthorization(user, FUNCTION_PRINTING_LOCAL);
		
		List list = new ArrayList();
		try {
			DMSTeam dmsTeam = UtilService.getDmsCache().getDMSTeamByUnit(teamUnit);
			dmsTeam.getPaperfile(true);
			Iterator iter = dmsTeam.getPaperfileChildren().iterator();
			while (iter.hasNext()) {
				DMSPaperfile dmsPaperfile = (DMSPaperfile) iter.next();
				String name = dmsPaperfile.getPapPhxName();
				if (name != null && !name.trim().equals("")) {
					list.add(new ItemPPFLine(name.trim()));
				}
			}
		}
		catch (DmsLinkException e) {
			throw new PhoenixException(e.toString());
		}

		//A-PUB, B-PUB and Cited docs only for EPO, not for NO
		org.epoline.phoenix.common.shared.ConfigManager config = org.epoline.phoenix.common.shared.ConfigManager
				.getInstance();
		String EPOOFFICE = config.getString("EPOOFFICE", "FALSE");

		if (EPOOFFICE.equalsIgnoreCase("TRUE")) {
			//cited docs must be double sided by default.
			ItemPPFLine cited = new ItemPPFLine(PaperfileCited.NAME);
			cited.setDoubleSided(true);
			list.add(cited);
			list.add(new ItemPPFLine(PaperfileAPubl.NAME));
			list.add(new ItemPPFLine(PaperfileBPubl.NAME));
		}

		Collections.sort(list, new ItemPPFLine("comp").getSimpleComparator());
		return list;
	}
	
	public List getTeamMembers(UserAuthenticator user, String teamUnit) throws PhoenixException, RemoteException {
		List memberList = new ArrayList();
		DMSTeam dmsTeam = UtilService.getDmsCache().getDMSTeamByUnit(teamUnit);
		if (dmsTeam == null) {
			return memberList;
			//throw new PhoenixObjectNotFoundException(teamUnit);
		}
		Collection dmsMembers = UtilService.getDmsCache().getDMSMemberItemsForDMSTeam(dmsTeam);
		for (Iterator iter = dmsMembers.iterator(); iter.hasNext();) {
			DMSMember dmsMember = (DMSMember) iter.next();
			DMSUser dmsUser = UtilService.getDmsCache().getDMSUserByKey(dmsMember.getMEMUSRKEY());
			memberList.add(new ItemUser(dmsUser.getUsrUserID().trim(), dmsUser.getUsrFullName().trim(), dmsUser
					.getKey()));
		}
		Collections.sort(memberList, new ItemUser("comp", "comp").getSimpleComparator());
		return memberList;
	}
	
	private BackendUtils getDmsManager() throws PhoenixException {
		if (_dmsManager == null) {
			_dmsManager = BackendUtils.getInstance();
		}
		return _dmsManager;
	}

	//returns a procedurale language for the given dossier
	private String getPROL(String dossierNr, String userID, String userPwd) throws PhoenixException {
		if (dossierNr == null || userID == null || userPwd == null) {
			throw new IllegalArgumentException("parameter is null");
		}
		if (dossierNr.equals("") || userID.equals("") || userPwd.equals("")) {
			throw new IllegalArgumentException("parameter is empty");
		}
		try {
			DSS_LegServerFactory.setConfig(SSLConfigManager.getInstance());
			String epasysConnectionString = SSLConfigManager.getInstance().getString("EPASYSCONNECTIONSTRING", "");
			String pipsConnectionString = SSLConfigManager.getInstance().getString("PIPSCONNECTIONSTRING", "");

			// Local use:
			DSS_LegServerFactory aControl = new DSS_LegServerFactory();

			ADClient.getInstance().setUserID(userID);
			ADClient.getInstance().setPassword(userPwd);

			DSS_LegServerLink tmpLink = aControl.getLegServerLink("EPASYS", epasysConnectionString);
			tmpLink.addItem(new DSS_ItemIdentifier("PROL", "001001"));
			tmpLink.refreshItems(dossierNr);
			LegacyServerItemInterface anItem = tmpLink.getItem("PROL");
			return anItem.getAsString();
		}
		catch (Exception x) {
			return null;
		}
	}


}