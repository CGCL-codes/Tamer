package com.hdmm.mediaserver.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileExistsException;
import org.jdom.JDOMException;
import org.primefaces.context.RequestContext;
import com.hdmm.mediaserver.development.DevLogger;
import com.hdmm.mediaserver.service.PlaylistService;

/**
 * PlaylistView.java 
 * Ist der Controller zur GUI (playlists.xthml).
 * @author Michael Tiede (20966946)
 * @version 16
 * @date 23.11.2011
 * @created 31.10.2011
 */
@Named
@SessionScoped
public class PlaylistView implements Serializable {

    private static final long serialVersionUID = 7914950308048789355L;

    private static DevLogger logger = new DevLogger(PlaylistView.class);

    private String selectedPlaylistName;

    private String newPlaylistName;

    private String deletePlaylistName;

    private String mergeSourcePlaylistName;

    private String currentUser;

    private boolean renderDetails;

    private List<String[]> playlistEntries;

    @Inject
    private PlaylistService service;

    private List<String> listOfPlaylists;

    /**
	 * Standardkonstruktor zum initallisieren von Variablen
	 */
    public PlaylistView() {
        listOfPlaylists = new ArrayList<String>();
        renderDetails = false;
    }

    /**
	 * Legt eine neue Playlist an
	 */
    public void createPlaylist() {
        try {
            service.createPlaylist(newPlaylistName, currentUser);
            logger.logInfoMessage("created new Playlist: " + newPlaylistName);
        } catch (FileExistsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "An error occurred! ", "Playlist \"" + newPlaylistName + "\" already exists. "));
            logger.logInfoMessage("createPlaylist: Playlist already exists");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred! ", "Please try again or contact the administrator. "));
            logger.logInfoMessage("createPlaylist: io exception");
        } catch (Exception e) {
            e.printStackTrace();
            logger.logInfoMessage("createPlaylist: other exception");
        }
        listOfPlaylists.add(newPlaylistName);
    }

    /**
	 * Hilfsmethode zum entfernen der Dateiendung ".xml"
	 * @param playlist - Dateiname der Playliste (z. B. example.xml)
	 * @return Name ohne ".xml"
	 */
    public String removeFileExtension(String playlist) {
        return playlist.substring(0, playlist.lastIndexOf('.'));
    }

    /**
	 * L�scht eine Playliste
	 */
    public void deletePlaylist() {
        renderDetails = false;
        try {
            switch(service.deletePlaylist(deletePlaylistName, currentUser)) {
                case 0:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You have no permission to delete the " + "playlist \"" + deletePlaylistName + "\". ", ""));
                    logger.logInfoMessage("delete playlist failed, user isn't owner");
                    break;
                case 1:
                    if (deletePlaylistName.equals(selectedPlaylistName)) {
                        selectedPlaylistName = "";
                    }
                    listOfPlaylists.remove(deletePlaylistName);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "The playlist \"" + deletePlaylistName + "\" was successfully deleted. ", ""));
                    logger.logInfoMessage("delete playlist successful: " + deletePlaylistName);
                    break;
                case 2:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured. The playlist \"" + deletePlaylistName + "\" could not be deleted. ", ""));
                    logger.logInfoMessage("delete playlist failed, even if user is owner");
                    break;
                default:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unknown error occured. The playlist \"" + deletePlaylistName + "\" could not be deleted. ", ""));
                    logger.logInfoMessage("delete playlist failed, unknown error");
                    break;
            }
        } catch (JDOMException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unknown error occured. The playlist \"" + deletePlaylistName + "\" could not be deleted. ", ""));
            e.printStackTrace();
            logger.logErrorMessage("deletePlaylist: jdom exception");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unknown error occured. The playlist \"" + deletePlaylistName + "\" could not be deleted. ", ""));
            e.printStackTrace();
            logger.logErrorMessage("deletePlaylist: io exception");
        }
    }

    /**
	 * Speichern einer umsortierten Playliste
	 */
    public void savePlaylistDetails() {
        String separatedString = null;
        do {
            separatedString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("hiddenSortedList");
        } while (separatedString == null || separatedString.equals(""));
        try {
            if (service.savePlaylistDetails(selectedPlaylistName, separatedString, currentUser)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "The playlist \"" + selectedPlaylistName + "\" was saved successfully. ", ""));
                logger.logInfoMessage("save playlist successful: " + selectedPlaylistName);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You are not allowed to change the playlist \"" + selectedPlaylistName + "\". ", "You are not the owner of this playlist."));
                logger.logInfoMessage("save playlist failed, user isn't owner");
            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred! ", "The playlist \"" + selectedPlaylistName + "\" could not be saved."));
            logger.logInfoMessage("savePlaylist: io exception");
        } catch (JDOMException e) {
            e.printStackTrace();
            logger.logInfoMessage("savePlaylist: jdom exception");
        } catch (Exception e) {
            e.printStackTrace();
            logger.logInfoMessage("savePlaylist: other exception");
        }
        RequestContext.getCurrentInstance().addCallbackParam("playlistSaved", true);
    }

    /**
	 * Fuegt die Playlisteintraege einer Playliste, einer anderen Playliste hinzu
	 * @param targetPlaylistName - Plaliste zu der Eintraege hinzugefuegt werden sollen
	 */
    public void mergePlaylists(String targetPlaylistName) {
        try {
            if (service.mergePlaylists(mergeSourcePlaylistName, targetPlaylistName, currentUser)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "The playlist \"" + mergeSourcePlaylistName + "\" and \"" + targetPlaylistName + "\" were merged successfully. ", ""));
                logger.logInfoMessage("save playlist successful: " + targetPlaylistName);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You are not allowed to merge playlists into the " + "playlist \"" + targetPlaylistName + "\". ", "You are not the owner of this playlist."));
                logger.logInfoMessage("delete playlist failed, user isn't owner");
            }
        } catch (JDOMException e) {
            e.printStackTrace();
            logger.logInfoMessage("mergePlaylist: jdom exception");
        } catch (IOException e) {
            e.printStackTrace();
            logger.logInfoMessage("mergePlaylist: io exception");
        } catch (Exception e) {
            e.printStackTrace();
            logger.logInfoMessage("mergePlaylist: other exception");
        }
    }

    public String getSelectedPlaylistName() {
        return selectedPlaylistName;
    }

    public void setSelectedPlaylistName(String selectedPlaylistName) {
        this.selectedPlaylistName = selectedPlaylistName;
    }

    public String getNewPlaylistName() {
        return newPlaylistName;
    }

    public void setNewPlaylistName(String newPlaylistName) {
        this.newPlaylistName = newPlaylistName;
    }

    public String getDeletePlaylistName() {
        return deletePlaylistName;
    }

    public void setDeletePlaylistName(String deletePlaylistName) {
        this.deletePlaylistName = deletePlaylistName;
    }

    public List<String> getListOfPlaylists() {
        listOfPlaylists = service.getAllPlaylists();
        return listOfPlaylists;
    }

    public void setListOfPlaylists(List<String> listOfPlaylists) {
        this.listOfPlaylists = listOfPlaylists;
    }

    public List<String[]> getPlaylistEntries() {
        try {
            playlistEntries = service.getPlaylistDetails(selectedPlaylistName);
        } catch (JDOMException e) {
            e.printStackTrace();
            logger.logErrorMessage("getPlaylistEntries:1");
        } catch (IOException e) {
            e.printStackTrace();
            logger.logErrorMessage("getPlaylistEntries:2");
        } catch (Exception e) {
            e.printStackTrace();
            logger.logErrorMessage("getPlaylistEntries:3");
        }
        return playlistEntries;
    }

    public void setPlaylistEntries(List<String[]> playlistEntries) {
        this.playlistEntries = playlistEntries;
    }

    public boolean isRenderDetails() {
        return renderDetails;
    }

    public void setRenderDetails(boolean renderDetails) {
        this.renderDetails = renderDetails;
    }

    public String getMergeSourcePlaylistName() {
        return mergeSourcePlaylistName;
    }

    public void setMergeSourcePlaylistName(String mergeSourcePlaylistName) {
        this.mergeSourcePlaylistName = mergeSourcePlaylistName;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}
