/** 
 * OGL Explorer
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.leo.oglexplorer.model.result;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.leo.oglexplorer.util.Util;

import com.sun.opengl.util.GLUT;

/**
 * SearchResult $Id: SearchResult.java 139 2011-08-16 07:54:13Z leolewis $.
 * 
 * <pre>
 * Each result type (Web, image, ...) must implements this
 * class to specify specific behaviors
 * </pre>
 * 
 * @author Leo Lewis
 */
public abstract class SearchResult {

	protected final static Color TITLE_COLOR = new Color(80, 80, 200);
	protected final static Color DESCRIPTION_COLOR = new Color(80, 80, 80);
	protected final static Color DESCRIPTION_COLOR2 = new Color(50, 50, 50);
	protected final static Color URL_COLOR = new Color(100, 200, 100);

	/** Index in the page */
	protected int _index;

	/** Search path or URL */
	protected String _path;

	/** Title */
	protected String _title;

	/** Description */
	protected String _description;

	/**
	 * Thread pool, static (one for all results), yes we compute images in a
	 * thread Use a parameterized ThreadPoolExecutor with 10 threads (which is
	 * the max used, so no more creation of thread) and a keep-alive of 1
	 * second. This allow a dramatic improve of performances when scrolling
	 * results.
	 */
	protected static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 1L,
			TimeUnit.SECONDS, new SynchronousQueue<Runnable>());;

	/** Is image creation is in progress */
	protected volatile boolean _isAdjusting;

	/** Thumbnail size */
	protected Dimension _thumbnailSize;

	/**
	 * SoftReference to the Thumbnail byte buffer. Allow to keep images in
	 * memory if available, to reuse them the as much as possible.
	 */
	protected SoftReference<ByteBuffer> _thumbnailByteBufferReference;

	/**
	 * Pending image, by search result type (make it static, it is useless to
	 * recompute it for each result type)
	 */
	public static Map<Class<? extends SearchResult>, ByteBuffer> _pendingImageMap = new HashMap<>();
	/** Pending image size */
	public static Map<Class<? extends SearchResult>, Dimension> _pendingImageSizeMap = new HashMap<>();

	/**
	 * Constructor
	 * 
	 * @param index result index
	 */
	public SearchResult(int index) {
		_index = index;
	}

	/**
	 * Return the value of the field index
	 * 
	 * @return the value of index
	 */
	public int getIndex() {
		return _index;
	}

	/**
	 * Return the value of the field path
	 * 
	 * @return the value of path
	 */
	public String getPath() {
		return _path;
	}

	/**
	 * Set the value of the field path
	 * 
	 * @param path the new path to set
	 */
	public void setPath(String path) {
		try {
			_path = URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			_path = path;
		}
	}

	/**
	 * Return the value of the field title
	 * 
	 * @return the value of title
	 */
	public String getTitle() {
		return _title;
	}

	/**
	 * Set the value of the field title
	 * 
	 * @param title the new title to set
	 */
	public void setTitle(String title) {
		try {
			_title = URLDecoder.decode(title, "UTF-8");
		} catch (Exception e) {
			_title = title;
		}
	}

	/**
	 * Return the value of the field description
	 * 
	 * @return the value of description
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Set the value of the field description
	 * 
	 * @param description the new description to set
	 */
	public void setDescription(String description) {
		try {
			_description = URLDecoder.decode(description, "UTF-8");
		} catch (Exception e) {
			_description = description;
		}
	}

	/**
	 * Image to draw on the GL for the given result. To be implemented
	 * 
	 * @return the image
	 */
	protected abstract BufferedImage imageToDrawImpl() throws Exception;

	/**
	 * Pending image to draw on the GL while waiting for real image to be
	 * computed
	 * 
	 * @return the image
	 */
	protected abstract Image pendingImageImpl();

	/**
	 * Open the result
	 */
	public abstract void open();

	/**
	 * Top drawing value
	 * 
	 * @return top
	 */
	protected abstract int top(int drawOrder);

	/**
	 * Left drawing value
	 * 
	 * @return left
	 */
	protected abstract int left(int drawOrder);

	/**
	 * Image to draw on the GL component. This method is not synchronized
	 * because we do not want to block the painting thread. Instead, we'll
	 * return a dummy image when the real image is being computed
	 * 
	 * @return the image
	 */
	private ByteBuffer imageToDraw() {
		ByteBuffer thumbnailByteBuffer = _thumbnailByteBufferReference != null ? _thumbnailByteBufferReference.get()
				: null;
		if (_isAdjusting) {
			// this one is synchronous, we need it now
			return pendingImage();
		} else if (thumbnailByteBuffer == null) {
			// compute the real image in a thread
			THREAD_POOL.execute(new Runnable() {
				public void run() {
					_isAdjusting = true;
					try {
						BufferedImage image = imageToDrawImpl();
						_thumbnailByteBufferReference = new SoftReference<>(Util.bufferedImagetoByteBuffer(image));
						_thumbnailSize = new Dimension(image.getWidth(), image.getHeight());
					} catch (Exception e) {
						e.printStackTrace();
						_thumbnailByteBufferReference = new SoftReference<>(pendingImage());
					} finally {
						_isAdjusting = false;
					}
				}
			});
		}
		return _thumbnailByteBufferReference != null ? _thumbnailByteBufferReference.get() : null;
	}

	/**
	 * Pending image in byte buffer format.
	 * 
	 * @return pending image
	 */
	private ByteBuffer pendingImage() {
		ByteBuffer pendingImg = _pendingImageMap.get(getClass());
		if (pendingImg == null) {
			BufferedImage bi = Util.toBufferImage(pendingImageImpl());
			pendingImg = Util.bufferedImagetoByteBuffer(bi);
			_pendingImageMap.put(getClass(), pendingImg);
			_pendingImageSizeMap.put(getClass(), new Dimension(bi.getWidth(), bi.getHeight()));
		}
		return pendingImg;
	}

	/**
	 * @see org.leo.oglexplorer.model.result.SearchResult#drawGL(javax.media.opengl.GL,
	 *      com.sun.opengl.util.GLUT, int, int, int)
	 */
	public void drawGL(GL gl, GLUT glut, GLU glu, int width, int height, int drawOrder) {
		try {
			// Change to projection matrix.
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();

			// Perspective.
			float widthHeightRatio = (float) width / (float) height;
			glu.gluPerspective(45, widthHeightRatio, 1, 20000);
			// camera
			setCamera(glu);

			// Change back to model view matrix.
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glLoadIdentity();

			Dimension dim = imageDimension();
			int normalizeDistance = (int) (1000 - drawOrder * 100);
			// draw the image on the GL
			gl.glBindTexture(GL.GL_TEXTURE_2D, 13);
			gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, dim.width, dim.height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE,
					imageToDraw());

			int left = left(drawOrder);
			int top = top(drawOrder);

			gl.glEnable(GL.GL_TEXTURE_2D);
			gl.glBindTexture(GL.GL_TEXTURE_2D, 13);
			gl.glBegin(GL.GL_POLYGON);
			gl.glTexCoord3d(0, 1, normalizeDistance);
			gl.glVertex3d(left, top, normalizeDistance);
			gl.glTexCoord3d(1, 1, normalizeDistance);
			gl.glVertex3d(left + dim.width, top, normalizeDistance);
			gl.glTexCoord3d(1, 0, normalizeDistance);
			gl.glVertex3d(left + dim.width, top + dim.height, normalizeDistance);
			gl.glTexCoord3d(0, 0, normalizeDistance);
			gl.glVertex3d(left, top + dim.height, normalizeDistance);

			gl.glEnd();
			gl.glFlush();
		} catch (IndexOutOfBoundsException e) {
			// ignore
		}
	}

	/**
	 * Set the camera
	 * 
	 * @param glu GLU
	 * @param drawOrder order of drawing
	 */
	protected abstract void setCamera(GLU glu);

	/**
	 * @see org.leo.oglexplorer.model.result.SearchResult#imageDimension()
	 */
	protected Dimension imageDimension() {
		if (!_isAdjusting && _thumbnailSize != null && _thumbnailByteBufferReference.get() != null) {
			return _thumbnailSize;
		} else {
			pendingImage();
			return _pendingImageSizeMap.get(getClass());
		}
	}

	/**
	 * Dispose the resources of the result (thumbnail), but not the result.
	 * Reload the resources before displaying to accelerate the display
	 * 
	 * @see SearchResult#preload()
	 */
	public void dispose() {
		// instead of unloading the image, but replace this mechanism with
		// SoftReferences
	}

	/**
	 * Preload the result (that has been dispose, to display it quickly when
	 * required)
	 * 
	 * @see SearchResult#dispose()
	 */
	public void preload() {
		imageToDraw();
	}
}
