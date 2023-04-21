package com.spukmk2me;

import com.spukmk2me.io.IFileSystem;
import com.spukmk2me.input.IInputMonitor;
import com.spukmk2me.video.IVideoDriver;
import com.spukmk2me.sound.ISoundMonitor;
import com.spukmk2me.scene.SceneManager;

/**
 *  The main device which hold everything of SPUKMK2me rendering engine.
 *  \details To access SPUKMK2me rendering engine, first you must create an
 * instance of Device class, and access things via this instance.\n
 *  There should be only one instance of Device at the same time
 * in your application. If there are more than one device, the result is
 * unpredictable.
 *  @see com.spukmk2me.scene.SceneManager
 *  @see com.spukmk2me.video.IVideoDriver
 *  @see com.spukmk2me.sound.ISoundMonitor
 *  @see com.spukmk2me.IInputMonitor
 */
public final class Device {

    private Device(IVideoDriver vdriver, ISoundMonitor smonitor, IInputMonitor inputMonitor, IFileSystem fileSystem, SceneManager scene) {
        m_soundMonitor = smonitor;
        m_videoDriver = vdriver;
        m_inputMonitor = inputMonitor;
        m_sceneManager = scene;
        m_fileSystem = fileSystem;
    }

    /**
     *  Setup a device manually. Not recommended for new developer.
     *  \details If you can manually setup all the components, you can forget
     * about Device object and this function as well.
     *  @param vdriver Video driver.
     *  @param smonitor Sound monitor.
     *  @param imonitor Input monitor.
     *  @param scene Scene manager.
     *  @return Device that hold all the components above.
     */
    public static Device CreateSPUKMK2meDevice(IVideoDriver vdriver, ISoundMonitor smonitor, IInputMonitor imonitor, IFileSystem fsystem, SceneManager scene) {
        return new Device(vdriver, smonitor, imonitor, fsystem, scene);
    }

    /**
     *  Get the current video driver.
     *  @return The current video driver.
     */
    public IVideoDriver GetVideoDriver() {
        return m_videoDriver;
    }

    /**
     *  Get the current sound monitor.
     *  @return The current sound monitor.
     */
    public ISoundMonitor GetSoundMonitor() {
        return m_soundMonitor;
    }

    /**
     *  Get the current input monitor.
     *  @return The current input monitor.
     */
    public IInputMonitor GetInputMonitor() {
        return m_inputMonitor;
    }

    /**
     *  Get the current file system.
     *  @return The current file system.
     */
    public IFileSystem GetFileSystem() {
        return m_fileSystem;
    }

    /**
     *  Get the current scene manager.
     *  @return The current scene manager.
     */
    public SceneManager GetSceneManager() {
        return m_sceneManager;
    }

    /**
     *  Stop the device and release all resource associated with it.
     *  \details Since the resource are managed by Java Virtual Machine,
     * all this function can do is nullify all references.
     */
    public void StopDevice() {
        if (m_soundMonitor != null) m_soundMonitor.Clear();
        m_videoDriver = null;
        m_sceneManager = null;
        m_inputMonitor = null;
        m_soundMonitor = null;
    }

    private IVideoDriver m_videoDriver;

    private SceneManager m_sceneManager;

    private IInputMonitor m_inputMonitor;

    private ISoundMonitor m_soundMonitor;

    private IFileSystem m_fileSystem;
}
