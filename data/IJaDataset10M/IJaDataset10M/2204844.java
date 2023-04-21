/*
 * one line to give the library's name and an idea of what it does.
 * Copyright (C) Curallucci de Peretti Emmanuel
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *  
*/
package adm.java.librairie;

import java.net.URLDecoder;
import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import adm.appli.implement.IhmObject;


public class JavaAdm extends Ihm{
	  public boolean ModeThrough    = false;
	  public boolean ModeAdm        = false;
	  public String AdmLink        = null;
	  public String AdmObjBase     = "";
	  public String AdmObjInfo     = null;
	  public String[] AdmObjTdLigne  = null;
	  public String AdmObjTarget   = "";
	  public String AdmForm        = "";
	  public String AdmSysContenu="";

	  public String current_rowid  = null;
	  public static String base_lg_browse = "lg_";	  
	  public ConnectDb IdConnect   = null;
	  private XmlDocument xmldoc   = new XmlDocument();

	  public JavaAdm(ConnectDb IdConnect,String NameObject) {
		super(IdConnect,NameObject,"PhpAdm");
	  }
	  
	  public boolean Initialise(Data object_source,String mode_gestion){
		  return true;
	  }
	  
	  public void InitObjectContexte(ConnectDb IdConnect){
		    this.IdConnect     = IdConnect;
		    this.AdmObjBase    = this.IdConnect.getValue("PROP_ID");
		    this.AdmObjTdLigne = (String[]) this.IdConnect.getValue("PROP_FCT").split("\\|");
		    this.AdmSysContenu = this.IdConnect.getValue("SYS_CONTENU");

		    if (this.IdConnect.debug){
		    	System.out.println("Debug for request parameter :");
			    Enumeration<?> enume;			    
			    enume = this.IdConnect.request.getParameterNames();
			    while (enume.hasMoreElements()) {
				      String name = (String) enume.nextElement();
				      System.out.println("  name :"+name+"="+this.IdConnect.request.getParameter(name));
				    }

			    System.out.println("Debug for session parameter :");
			    enume = this.IdConnect.request.getSession().getAttributeNames();
			    while (enume.hasMoreElements()) {
				      String name = (String) enume.nextElement();
				      System.out.println("name :"+name+"="+this.IdConnect.request.getSession().getAttribute(name));
				    }
		    }
	  }
	  
	  @SuppressWarnings("unchecked")
	public void ResetUserFrame(){
  		// Pour tous les elements catalogue de type viewer ou browse alors vidage du buffer userframe
			Iterator<?> i = this.IdConnect.CatalogueObject.GetObject().entrySet().iterator(); 
			while (i.hasNext()){
				Object o = i.next();
				Map.Entry entree = (Map.Entry)o;	
				Ihm ObjCat = (Ihm) entree.getValue();
				
				if (ObjCat.GetLinkSource("tableio") !=null && ObjCat.UserFrame !=null) ObjCat.UserFrame.InitRef();
				else
				if (ObjCat instanceof Browse && ObjCat.UserFrame !=null) ObjCat.UserFrame.InitRef();
			}				  
	  }
	  
	  @SuppressWarnings({ "unchecked", "deprecation" })
	public void AnalyseUserFrame(){
			Iterator<?> i = this.IdConnect.CatalogueObject.GetObject().entrySet().iterator(); 
			while (i.hasNext()){
				Object o = i.next();
				Map.Entry entree = (Map.Entry)o;	
				Ihm ObjCat = (Ihm) entree.getValue();
				
				if (ObjCat.GetLinkSource("tableio") !=null && ObjCat.UserFrame !=null) {
					// Analayse des composants de l'object pour savoir si il existe des composants de type date
					// et si oui, on essaie de les normalisés
					Data objsource = ObjCat.DataSource();
					if (objsource != null){					
						Iterator iObject = objsource.ColFormat.entrySet().iterator();					
						while (iObject.hasNext()){
							Object oObject = iObject.next();
							Map.Entry eObject = (Map.Entry)oObject;
							
							ObjRowUnique zone = (ObjRowUnique) eObject.getValue();
							
							if (zone.type.equalsIgnoreCase("date")){
								if (ObjCat.UserFrame.IsExisteName(zone.inside)){
									// Voir toutes les zones qui commence par suivi de [
									if (!ObjCat.UserFrame.GetValueToSql(zone.inside).equals("")){
										String tmp = ObjCat.UserFrame.GetValueToSql(zone.inside).replaceAll("[-./ ]", "");
										boolean ok = false;
										Date aujourdhui = new Date();
										if (tmp.length()==4){
											// Ajout de l'année
											tmp = tmp+BaseIhm.sprintf("%04d", aujourdhui.getYear()+1900);
											ok = true;
										}
										else
										if (tmp.length()==6){
											// Ajout de l'année
											// Voir si le chiffre année est superieur à l'année en cours alors -1
											tmp = substring(tmp,0,4)+BaseIhm.sprintf("%02d", (int) (aujourdhui.getYear()+1900) / 100)+substring(tmp,4,2);
											ok = true;
										}
										else
										if (tmp.length()==8){
											ok = true;
										}
										tmp = substring(tmp, 0,2)+"/"+substring(tmp, 2,2)+"/"+substring(tmp, 4,4);
										if (ok && !ObjCat.UserFrame.GetValueToSql(zone.inside).equals(tmp)){
											ObjCat.UserFrame.SetValue(zone.inside, tmp);
											print (ModElementValue(ObjCat.GetFormName(), zone.inside, tmp));
										}
									}
								}
							}
						}
					}
				}
				else{
					if (ObjCat.UserFrame != null){
						if (ObjCat instanceof Viewer){
							Iterator iObject = ((Viewer)ObjCat).ColFormat.entrySet().iterator();					
							while (iObject.hasNext()){
								Object oObject = iObject.next();
								Map.Entry eObject = (Map.Entry)oObject;
								
								ObjRowUnique zone = (ObjRowUnique) eObject.getValue();
								
								if (zone.type.equalsIgnoreCase("date")){
									if (ObjCat.UserFrame.IsExisteName(zone.inside)){
										if (!ObjCat.UserFrame.GetValueToSql(zone.inside).equals("")){
											String tmp = ObjCat.UserFrame.GetValueToSql(zone.inside).replaceAll("[-./ ]", "");
											boolean ok = false;
											Date aujourdhui = new Date();
											if (tmp.length()==4){
												// Ajout de l'année
												tmp = tmp+BaseIhm.sprintf("%04d", aujourdhui.getYear()+1900);
												ok = true;
											}
											else
											if (tmp.length()==6){
												// Ajout de l'année
												tmp = substring(tmp,0,4)+BaseIhm.sprintf("%02d", (int) (aujourdhui.getYear()+1900) / 100)+substring(tmp,4,2);
												ok = true;
											}
											else
											if (tmp.length()==8){
												ok = true;
											}
											tmp = substring(tmp, 0,2)+"/"+substring(tmp, 2,2)+"/"+substring(tmp, 4,4);
											if (ok && !ObjCat.UserFrame.GetValueToSql(zone.inside).equals(tmp)){
												ObjCat.UserFrame.SetValue(zone.inside, tmp);
												print (ModElementValue(ObjCat.GetFormName(), zone.inside, tmp));
											}
										}
									}
								}
							}
						}
							
						}
					}
				}
			}
	  
	  
	  @SuppressWarnings({ "unchecked", "deprecation" })
	public synchronized void InitObject(Data ObjData,ConnectDb IdConnect){
		    this.InitObjectContexte(IdConnect);
		    this.ResetUserFrame();
		    
		    //System.out.println("InitObject java :");
		    // Decodage de l'information AdmSysContenu		    
		    if (this.AdmSysContenu.indexOf("<?xml") == 0){	
		    	//System.out.println("XML :"+this.AdmSysContenu);
  	          	Ihm ObjectIhm;
  	          	NodeList ListeNodeFrame,ListeNode;
  	          	Node ndListeNodeFrame;
  	          	int iListeNodeFrame;
  	          	Hashtable<String,FieldValue> Liste_attr = null;
  	            Hashtable<String,FieldValue>Liste_attr_valeur = null;
  	            Iterator<?> i;
  	            Object o;
  	            Map.Entry entree;
	    			    		
	    		if (this.xmldoc.LoadXmlString(this.AdmSysContenu)){
		    	    Node NodeContenu = this.xmldoc.getNode("CONTENU");
		    	    		    	    
		    	    //System.out.println(xmldoc.SaveXml());
		    	    if (NodeContenu != null){
		    	    	// Analyse du contenu des differentes form et donc des valeurs des champs ensuite
		    	    	ListeNode = NodeContenu.getChildNodes();
		    	    	String NomForme = "",Name="",Value="";
		    	        
		    	    	if (this.IdConnect.debug) System.out.println("Flux :"+this.xmldoc.SaveXml());
		    	    	int iListe = ListeNode.getLength();
		    	    	for(int icpt=0; icpt < iListe; icpt++){
		    	          Node nd = ListeNode.item(icpt);
		    	          switch (nd.getNodeType())
		    	          {
		    	            case org.w3c.dom.Node.ELEMENT_NODE:
		    	              if (nd.getNodeName().equals("FORM"))
		    	              {
		    	            	  Liste_attr = XmlDocument.GetListeAttr(nd);
		    	            	  
		  						  i = Liste_attr.entrySet().iterator(); 
								  while (i.hasNext()){
									o = i.next();
									entree = (Map.Entry)o;								
									
									FieldValue element = (FieldValue) entree.getValue();
		    	            	  		    	            	  
		    	            		  Name  = element.FieldName;
		    	            		  Value = element.FieldValue.toString();
		    	            		  		    	            		  
		    	            		  if (Name.equals("name")){
		    	            			  // Le nom de l'ecran est associé à l'object du catalogue
		    	            			  NomForme = Value.substring(8);
		    	            			
		    	            			  if (this.IdConnect.debug) System.out.println("Name = "+NomForme);
		    	            			  
		    	            			  ObjectIhm = (Ihm) this.IdConnect.CatalogueObject.GetReferenceObject(NomForme);
		    	            			  		    	        		    	            			  
		    	            			  if (ObjectIhm == null) {
		    	            				  //System.out.println("JavaAdm Object non trouve dans le catalogue :"+NomForme);
		    	            			  }
		    	            			  else{
			    	            			  ObjectIhm.CollecteData();
			    	            			  
			    	            			  Node NodeValeur = this.xmldoc.getNode(nd,"VALEUR");
			    	            			  
			    	            			  if (NodeValeur != null){
				    	            			  ListeNodeFrame = NodeValeur.getChildNodes();
				    	            			  
				    	            			  iListeNodeFrame = ListeNodeFrame.getLength();
				    	  		    	    	  for(int j=0; j < iListeNodeFrame; j++){
				    	  		    	    		  ndListeNodeFrame = ListeNodeFrame.item(j);
				    	  		    	    		  Name = ndListeNodeFrame.getNodeName();
				    	  		    	    		  
				    	  		    	    		  Name=Name.replace(".d","[").replace(".e","]");
				    	  		    	    		  
				    	  		    	    		  Liste_attr_valeur = XmlDocument.GetListeAttr(ndListeNodeFrame);
				    	  		    	    		  				    	  		    	    		  
				    	  		    	    		  if (Liste_attr_valeur.get("value") !=null){
				    	  		    	    			Value = Decode(URLDecoder.decode (Liste_attr_valeur.get("value").FieldValue.toString()));
				    	  		    	    			//Value=Value.replace(".d","[").replace(".e","]");
				    	  		    	    			ObjectIhm.UserFrame.SetValue(Name,Value);
				    	  		    	    		  }
				    	  		    	    		  else
				    	  		    	    			ObjectIhm.UserFrame.SetValue(Name,"");
				    			    	          }		    	            				  
			    	            			  }
		    	            			  }
		    	            		  }
		    	            	  }
		    	              }		    	              
		    	          }
		    	        }
		    	    }
	    		}
  	          	if (Liste_attr != null) Liste_attr.clear();
  	            if (Liste_attr_valeur !=null) Liste_attr_valeur.clear();

		    }
		    
		    this.AnalyseUserFrame();
		    		    
		    
		    HideMenu();
		    
		    if (IdConnect.debug){
		    	System.out.println(" DBG this.AdmObjBase="+this.AdmObjBase);
		    	System.out.println(" DBG    ObjData     ="+ObjData);
		    }
		    if (this.AdmObjBase.equals("initialise"))
		    {
		    	if (ObjData != null) ObjData.Initialise(null, null);
		    }
		    else
		    if (this.AdmObjBase.equals("reload")){
		    	
		    }
		    else // Recherche par rapport au nom de l'object dans le catalogue des objects
		    {				    	
		    	Ihm object_ret = (Ihm) this.IdConnect.CatalogueObject.GetReferenceObject(this.AdmObjBase);
		    	
		    	if (object_ret == null){
		    		System.err.println("ERREUR recherche reference "+this.AdmObjBase+" dans "+this.getClass().getName());
		    	}
		    	else
		    	if (this.AdmObjTdLigne != null){			    		
		    		//System.out.println("LoadSysB = "+this.con.JavaAdm.AdmObjTdLigne.length+"\n");
		    				    		
		    		if (this.AdmObjTdLigne[0].indexOf(base_lg_browse) == 0){
		    						    			
		    			int index_ligne =  Integer.valueOf(this.AdmObjTdLigne[0].substring(3));
		    	        
		    	        Data object_source =  (Data) object_ret.DataSource();

		    	        if (object_source.IsEnabled()) return;			    	       
		    	        
		    	        if (this.AdmObjTdLigne.length >1){
			    	        object_source.FindPosition(object_ret,index_ligne);
	    	        		if (object_ret instanceof Browse) {
								Browse new_name = (Browse) object_ret;
								new_name.Record_tmp = object_source.GetCurrentDb();
							}
		    	        	if (this.AdmObjTdLigne[1].trim().equalsIgnoreCase("dblclick")){
		    	        		object_ret.Initialise(object_source, "");
		    	        		((IhmObject) object_ret).Operation("dblclick");
		    	        	}
		    	        	else
		    	        	if (this.AdmObjTdLigne[1].trim().equalsIgnoreCase("menu")){
		    	        		object_ret.AffMenuItem();
		    	        	}
		    	        	else
		    	        	if(Integer.valueOf(this.AdmObjTdLigne[1]) > 1){
		    	        		object_source.TraiteLigne();
		    	        	}
		    	        	else{
		    	        		((IhmObject) object_ret).Operation("click");
		    	        	}
		    	        }
		    		}			    	
			    	else
			    	if (this.AdmObjTdLigne[0].equals("sys_null"))
			    	{
			    		// RIEN
			    	}
			    	else
			    	if (this.AdmObjTdLigne[0].equals("sys_reload"))
			    	{			    		
			    		if (object_ret !=null){
			    			Data object_source = (Data) object_ret.DataSource();
				    		if (object_source !=null){
					    		object_source.sys_reload();
					    		object_source.SetSessionAdm("PropageOK","OUI");					    		
				    		}
				    		else{
				    			System.out.println("JavaAdm DataSource de l'object non trouve : "+this.AdmObjBase);
				    		}
				    	}
			    		else{
			    			System.out.println("JavaAdm Sys_reload object non connu : "+this.AdmObjBase);
			    		}
			    	}
			    	else
			    	if (this.AdmObjTdLigne[0].equals("sys_init"))
			    	{
			    		Data object_source = (Data) object_ret.DataSource();
			    		if (!object_source.GetSessionAdmString("PropageOK").equals("OUI"))
			    		{				    		
				    		object_source.sys_reload();
				    		object_source.SetSessionAdm("PropageOK","OUI");
			    		}				    		
			    	}
			    	else
			    	if (this.AdmObjTdLigne[0].equals("sys_refresh"))
			    	{				    		
			    		this.lancerMethode(object_ret,null,"DisplayRecord");
			    	}
			    	else
			    	{				    		
			    		Data object_source = (Data) object_ret.DataSource();
			    		
			    		if (object_ret != null){
			    			boolean fOk = true;
			    			
			    			if (this.AdmObjTdLigne[0].equalsIgnoreCase("sys_initial")) object_ret.print("IHMObject.sys_initial = true;");
			    			if (object_source != null && this.AdmObjTdLigne[0].equalsIgnoreCase("sys_initial")){
			    				if (object_source.IsEnabled()){
			                        BaseIhm.ErrJs("Veuillez valider ou annuler l'action en cours sur l'écran concernant : "+
			                        			(object_source.lib_user_object.equals("") ? object_source.name_object : object_source.lib_user_object));
			                        fOk = false;
			                        object_ret.print("IHMObject.sys_initial = false;");
			                    }
			    			}
				    		if (fOk) {
				    			if (!object_ret.getRefresh()){
				    				object_ret.Initialise(object_source, "");
				    			}
				    			//this.lancerMethode(object_ret,object,"Operation");
				    			((IhmObject) object_ret).Operation(this.AdmObjTdLigne[0].toString());
				    		}
				    		else{
				    			System.out.println("Operation non lancée dans "+object_ret.getClass().getName());
				    		}
			    		}
			    		else{
			    			System.out.println("Class not found :"+this.AdmObjBase);
			    		}
			    	}
		    	}
		    }
		    // Nettoyage de toutes les piles sql actives
		    IdConnect.RemoveMemory();
		    // Nettoyage du document
		    xmldoc.clear();
		    this.AdmSysContenu = null;
		    this.AdmForm = null;
		    this.AdmObjInfo = null;
		    this.AdmObjTdLigne = null;
	  }
}
