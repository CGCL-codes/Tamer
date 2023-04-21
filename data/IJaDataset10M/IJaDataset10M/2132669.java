package com.dukesoftware.ongakumusou3d.draw.artist;

import javax.media.opengl.GL;
import com.dukesoftware.ongakumusou3d.data.parameter.manager.AlbumParameter;
import com.dukesoftware.ongakumusou3d.data.parameter.manager.ArtistParameter;
import com.dukesoftware.ongakumusou3d.opengl.model3d.album.AlbumCase;
import com.dukesoftware.utils.opengl.camera.CameraPosition;

/**
 * 
 * <p></p>
 *
 * <h5>update history</h5> 
 * <p>2007/06/05 This file was created.</p>
 *
 * @author 
 * @since 2007/06/05
 * @version last update 2007/06/05
 */
public class ArtistFirstOpen2 extends ArtistFirstOpen {

    public ArtistFirstOpen2(AlbumCase albumCase, CameraPosition cameraPosition, float[] mat) {
        super(albumCase, cameraPosition, mat);
    }

    @Override
    public void display(GL gl, ArtistParameter artistParameter, AlbumParameter albumParameter) {
        albumParameter.setRelativeLocationStrict(0, 0, 0);
        artistParameter.status.disableFirstOpen();
        albumCase.setAllandRender(gl, albumParameter, mat);
    }
}
