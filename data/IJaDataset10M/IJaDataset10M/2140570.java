package xbrowser.options;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import xbrowser.XRepository;
import xbrowser.util.*;

public class XSerializerOptionPage extends XOptionPage {

    public XSerializerOptionPage() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(gridbag);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 2, 5, 2);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "FileNameDesc"), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "BookmarkSerializer"), this, gridbag, constraints, 1, 1);
        addToContainer(cmbBookmarkSerializer, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "BookmarkFileName"), this, gridbag, constraints, 1, 1);
        addToContainer(txfBookmarkFileName, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "HistorySerializer"), this, gridbag, constraints, 1, 1);
        addToContainer(cmbHistorySerializer, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "HistoryFileName"), this, gridbag, constraints, 1, 1);
        addToContainer(txfHistoryFileName, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "PluginSerializer"), this, gridbag, constraints, 1, 1);
        addToContainer(cmbPluginSerializer, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "PluginFileName"), this, gridbag, constraints, 1, 1);
        addToContainer(txfPluginFileName, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "CookieSerializer"), this, gridbag, constraints, 1, 1);
        addToContainer(cmbCookieSerializer, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "CookieFileName"), this, gridbag, constraints, 1, 1);
        addToContainer(txfCookieFileName, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "CacheIndexSerializer"), this, gridbag, constraints, 1, 1);
        addToContainer(cmbCacheIndexSerializer, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        addToContainer(XRepository.getComponentBuilder().buildLabel(this, "CacheIndexFileName"), this, gridbag, constraints, 1, 1);
        addToContainer(txfCacheIndexFileName, this, gridbag, constraints, 1, 1);
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weighty = 1;
        addToContainer(Box.createGlue(), this, gridbag, constraints, GridBagConstraints.REMAINDER, 1);
    }

    public String getName() {
        return XRepository.getResourceManager().getProperty(this, "Title");
    }

    public ImageIcon getIcon() {
        return XRepository.getComponentBuilder().buildImageIcon(this, "image.icon");
    }

    public void loadInfo() {
        if (cmbBookmarkSerializer.getItemCount() == 0) {
            Iterator bm_serializers = XRepository.getConfiguration().getBookmarkSerializers();
            while (bm_serializers.hasNext()) cmbBookmarkSerializer.addItem(bm_serializers.next());
        }
        cmbBookmarkSerializer.setSelectedItem(XRepository.getConfiguration().getActiveBookmarkSerializer());
        txfBookmarkFileName.setText(XRepository.getConfiguration().getBookmarkFileName());
        if (cmbHistorySerializer.getItemCount() == 0) {
            Iterator history_serializers = XRepository.getConfiguration().getHistorySerializers();
            while (history_serializers.hasNext()) cmbHistorySerializer.addItem(history_serializers.next());
        }
        cmbHistorySerializer.setSelectedItem(XRepository.getConfiguration().getActiveHistorySerializer());
        txfHistoryFileName.setText(XRepository.getConfiguration().getHistoryFileName());
        if (cmbPluginSerializer.getItemCount() == 0) {
            Iterator plugin_serializers = XRepository.getConfiguration().getPluginSerializers();
            while (plugin_serializers.hasNext()) cmbPluginSerializer.addItem(plugin_serializers.next());
        }
        cmbPluginSerializer.setSelectedItem(XRepository.getConfiguration().getActivePluginSerializer());
        txfPluginFileName.setText(XRepository.getConfiguration().getPluginFileName());
        if (cmbCookieSerializer.getItemCount() == 0) {
            Iterator cookie_serializers = XRepository.getConfiguration().getCookieSerializers();
            while (cookie_serializers.hasNext()) cmbCookieSerializer.addItem(cookie_serializers.next());
        }
        cmbCookieSerializer.setSelectedItem(XRepository.getConfiguration().getActiveCookieSerializer());
        txfCookieFileName.setText(XRepository.getConfiguration().getCookieFileName());
        if (cmbCacheIndexSerializer.getItemCount() == 0) {
            Iterator cache_index_serializers = XRepository.getConfiguration().getCacheIndexSerializers();
            while (cache_index_serializers.hasNext()) cmbCacheIndexSerializer.addItem(cache_index_serializers.next());
        }
        cmbCacheIndexSerializer.setSelectedItem(XRepository.getConfiguration().getActiveCacheIndexSerializer());
        txfCacheIndexFileName.setText(XRepository.getConfiguration().getCacheIndexFileName());
    }

    public void saveInfo() {
        XRepository.getConfiguration().setActiveBookmarkSerializer((XBookmarkSerializerObject) cmbBookmarkSerializer.getSelectedItem());
        XRepository.getConfiguration().setActiveHistorySerializer((XHistorySerializerObject) cmbHistorySerializer.getSelectedItem());
        XRepository.getConfiguration().setActivePluginSerializer((XPluginSerializerObject) cmbPluginSerializer.getSelectedItem());
        XRepository.getConfiguration().setActiveCookieSerializer((XCookieSerializerObject) cmbCookieSerializer.getSelectedItem());
        XRepository.getConfiguration().setActiveCacheIndexSerializer((XCacheIndexSerializerObject) cmbCacheIndexSerializer.getSelectedItem());
        XRepository.getConfiguration().setBookmarkFileName(txfBookmarkFileName.getText().trim());
        XRepository.getConfiguration().setHistoryFileName(txfHistoryFileName.getText().trim());
        XRepository.getConfiguration().setPluginFileName(txfPluginFileName.getText().trim());
        XRepository.getConfiguration().setCookieFileName(txfCookieFileName.getText().trim());
        XRepository.getConfiguration().setCacheIndexFileName(txfCacheIndexFileName.getText().trim());
    }

    private JComboBox cmbBookmarkSerializer = new JComboBox();

    private JComboBox cmbHistorySerializer = new JComboBox();

    private JComboBox cmbPluginSerializer = new JComboBox();

    private JComboBox cmbCookieSerializer = new JComboBox();

    private JComboBox cmbCacheIndexSerializer = new JComboBox();

    private JTextField txfBookmarkFileName = new JTextField(20);

    private JTextField txfHistoryFileName = new JTextField(20);

    private JTextField txfPluginFileName = new JTextField(20);

    private JTextField txfCookieFileName = new JTextField(20);

    private JTextField txfCacheIndexFileName = new JTextField(20);
}
