package org.jdna.sagetv.networkencoder.sage7;

import org.apache.log4j.Logger;
import org.jdna.sagetv.networkencoder.NetworkTunerServer;
import org.jdna.sagetv.networkencoder.ServerConfiguration;
import sage.SageTVPluginRegistry;
import sagex.api.Configuration;
import sagex.phoenix.Phoenix;
import sagex.phoenix.configuration.Group;
import sagex.phoenix.configuration.proxy.GroupParser;
import sagex.phoenix.configuration.proxy.GroupProxy;
import sagex.phoenix.plugin.PluginConfigurationHelper;
import sagex.plugin.AbstractPlugin;
import sagex.plugin.ConfigValueChangeHandler;
import sagex.util.Log4jConfigurator;

public class DummyNetworkEncoderPlugin extends AbstractPlugin {

    private Logger log = Logger.getLogger(this.getClass());

    private NetworkTunerServer tuner = new NetworkTunerServer();

    private ServerConfiguration config = null;

    public DummyNetworkEncoderPlugin(SageTVPluginRegistry registry) {
        super(registry);
        try {
            Log4jConfigurator.configureQuietly("sagex-networkencoder");
            config = GroupProxy.get(ServerConfiguration.class);
            Group group = GroupParser.parseGroup(ServerConfiguration.class);
            Phoenix.getInstance().getConfigurationMetadataManager().addMetadata(group);
            PluginConfigurationHelper.addConfiguration(this, group);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            super.start();
            updateDeviceConfiguration();
            log.info("Starting Dummy Network Tuner");
            tuner.startServer();
        } catch (Throwable t) {
            log.warn("start() failed: ", t);
            t.printStackTrace();
        }
    }

    private void updateDeviceConfiguration() {
        String encoderId = "123";
        Configuration.SetProperty(String.format("mmc/encoders/%s/1/0/video_crossbar_index", encoderId), "0");
        Configuration.SetProperty(String.format("mmc/encoders/%s/1/0/video_crossbar_type", encoderId), "1");
        Configuration.SetProperty(String.format("mmc/encoders/%s/capture_config", encoderId), "2050");
        Configuration.SetProperty(String.format("mmc/encoders/%s/encoder_merit", encoderId), "0");
        Configuration.SetProperty(String.format("mmc/encoders/%s/encoding_host", encoderId), String.format("localhost:%s", config.getPort()));
        Configuration.SetProperty(String.format("mmc/encoders/%s/encoding_host_login_md5", encoderId), "");
        Configuration.SetProperty(String.format("mmc/encoders/%s/video_capture_device_name", encoderId), config.getDeviceName());
        Configuration.SetProperty(String.format("mmc/encoders/%s/video_capture_device_num", encoderId), "0");
        Configuration.SaveProperties();
    }

    @Override
    public void stop() {
        super.stop();
        log.info("Stopping Dummy Network Tuner");
        tuner.stopServer();
    }

    @ConfigValueChangeHandler("jdna/networktuner/dummy/discoveryEnabled")
    public void onDiscoveryChanged() {
        tuner.restart();
    }

    @ConfigValueChangeHandler("jdna/networktuner/dummy/port")
    public void onPortChanged() {
        tuner.restart();
    }

    @ConfigValueChangeHandler("jdna/networktuner/dummy/recordingFile")
    public void onRecordingFileChanged() {
        tuner.restart();
    }

    @ConfigValueChangeHandler("jdna/networktuner/dummy/deviceName")
    public void onNameChanged() {
        updateDeviceConfiguration();
    }
}
