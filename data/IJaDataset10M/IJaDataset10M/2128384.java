package org.fxplayer.rest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fxplayer.domain.PlayList;
import org.fxplayer.domain.SmartPlayList;
import org.fxplayer.domain.Track;
import org.fxplayer.domain.User;
import org.fxplayer.rest.representations.PlayListRepresentation;
import org.fxplayer.rest.representations.TrackInfoRepresentation;
import org.fxplayer.rest.representations.TrackRepresentation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.sun.jersey.api.view.Viewable;

/**
 * The Class PlaylistResource.
 */
public class PlaylistResource extends AbstractResource {

    /** The Constant LOG. */
    public static final Log LOG = LogFactory.getLog(PlaylistResource.class);

    /** The playlist id. */
    @PathParam("playlistId")
    private int playlistId;

    /**
	 * Delete playlist player.
	 * @return the response
	 */
    @DELETE
    @Transactional
    public Response deletePlaylistPlayer() {
        final User user = retrieveUser();
        if (user.isRole(User.ROLE_ANONYMOUS)) failAuth("Anonymous user cannot delete playlists");
        final PlayList plst = PLAYLIST_DAO.findById(playlistId);
        if (plst == null) failNotFound("PlayList id:" + playlistId + " not found");
        if (LOG.isTraceEnabled()) LOG.trace("Deleting Playlist:id=" + playlistId);
        if (plst.getClass().equals(SmartPlayList.class)) return Response.serverError().build();
        user.getPlayLists().remove(plst);
        PLAYLIST_DAO.delete(plst, false);
        return Response.ok().build();
    }

    /**
	 * Gets the playlist.
	 * @return the playlist
	 */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Transactional
    public Object getPlaylist() {
        final User user = retrieveUser();
        if (!user.isRole(User.ROLE_ANONYMOUS)) retrieveFolders(user);
        final PlayList plst = PLAYLIST_DAO.findById(playlistId);
        if (plst == null) failNotFound("PlayList id:" + playlistId + " not found");
        if (LOG.isTraceEnabled()) LOG.trace("Getting Playlist:id=" + playlistId);
        final PlayList pl = PLAYLIST_DAO.reloadPlayList(plst);
        if (!user.isRole(User.ROLE_ANONYMOUS)) return pl;
        final Collection<Track> tracks = TRACK_DAO.listIn(pl.getTrackIds());
        final Collection<TrackRepresentation> tracksRepr = new ArrayList<TrackRepresentation>(tracks.size());
        for (final Track track : tracks) tracksRepr.add(new TrackRepresentation(track));
        final TrackInfoRepresentation tir = new TrackInfoRepresentation(tracks);
        return new PlayListRepresentation(pl, tracksRepr, tir);
    }

    /**
	 * Gets the playlist player.
	 * @return the playlist player
	 */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getPlaylistPlayer() {
        final PlayList plst = PLAYLIST_DAO.findById(playlistId);
        if (plst == null) failNotFound("PlayList id:" + playlistId + " not found");
        if (LOG.isTraceEnabled()) LOG.trace("Getting Playlist:id=" + playlistId);
        final ResponseBuilder builder = Response.ok(new Viewable("/index.jsp", PLAYLIST_DAO.reloadPlayList(plst))).type(MediaType.TEXT_HTML);
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        cacheControl.setMaxAge(-1);
        return builder.cacheControl(cacheControl).build();
    }

    /**
	 * Put playlist.
	 * @param name
	 *          the name
	 * @param trackIdsStr
	 *          the track ids str
	 * @param append
	 *          the append
	 * @return the response
	 * @throws URISyntaxException
	 *           the URI syntax exception
	 */
    @PUT
    @Transactional(propagation = Propagation.REQUIRED)
    public Response putPlaylist(@FormParam("name") final String name, @FormParam("trackIds") final String trackIdsStr, @FormParam("append") final Boolean append) throws URISyntaxException {
        final User user = retrieveUser();
        final PlayList plst = PLAYLIST_DAO.findById(playlistId);
        if (LOG.isTraceEnabled()) LOG.trace("Saving PlayList:id=" + plst.getId());
        if (plst == null) failNotFound("PlayList id:" + playlistId + " not found");
        if (!plst.getUser().equals(user)) failAuth("Authorization denied to PlayList id : " + playlistId);
        PLAYLIST_DAO.reloadPlayList(plst);
        if (name != null) plst.setName(name);
        if (append != null && !append) PLAYLIST_DAO.delete(plst, true);
        if (trackIdsStr != null) {
            final Set<Integer> trackIds = new LinkedHashSet<Integer>();
            for (final String trackIdStr : trackIdsStr.split(",")) if (trackIdStr.length() > 0) trackIds.add(Integer.parseInt(trackIdStr));
            plst.addTracks(trackIds);
        }
        PLAYLIST_DAO.save(plst);
        return Response.ok().entity(new PlayListRepresentation(plst)).build();
    }
}
