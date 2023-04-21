
package org.makagiga.plugins.kidsicontheme;

import java.util.HashMap;

import org.makagiga.commons.MIcon;
import org.makagiga.commons.icons.DefaultIconLoader;
import org.makagiga.plugins.LookAndFeelPlugin;
import org.makagiga.plugins.PluginException;

public final class Plugin extends LookAndFeelPlugin {

	// private

	private final HashMap<String, String> map = new HashMap<>();

	// public

	public Plugin() { }

	@Override
	public void onInit() throws PluginException {
		super.onInit();

		addAction("back", "ui/left");
		addAction("bookmark", "ui/bookmark");
		addAction("button_cancel", "ui/cancel");
		addAction("button_ok", "ui/ok");
		addAction("compfile", "ui/download");
		addAction("configure", "ui/configure");
		addAction("decrypted", "ui/unlocked");
		addAction("down", "ui/down");
		addAction("editcopy", "ui/copy");
		addAction("editcut", "ui/cut");
		addAction("editdelete", "ui/delete");
		addAction("editpaste", "ui/paste");
		addAction("encrypted", "ui/locked");
		addAction("exit", "ui/quit");
		addAction("fileclose", "ui/close");
		addAction("filefind", "ui/find");
		addAction("filenew", "ui/newfile");
		addAction("fileopen", "ui/open");
		addAction("fileprint", "ui/print");
		addAction("filesave", "ui/save");
		addAction("fonts", "ui/font");
		addAction("forward", "ui/right");
		addAction("help", "ui/help");
		addAction("misc", "ui/misc");
		addAction("next", "ui/next");
		addAction("previous", "ui/previous");
		addAction("redo", "ui/redo");
		addAction("reload", "ui/refresh");
		addAction("stop", "ui/stop");
		addAction("undo", "ui/undo");
		addAction("up", "ui/up");
		addAction("view_multicolumn", "ui/multicolumnview");
		addAction("view_text", "ui/detailedview");
		addAction("view_tree", "ui/tree");
		addAction("viewmag+", "ui/zoomin");
		addAction("viewmag-", "ui/zoomout");
		addAction("window_fullscreen", "ui/fullscreen");

		addApp("date", "ui/calendar");
		addApp("konsole", "ui/console");
		addApp("locale", "ui/locale");

		addFS("desktop", "ui/desktop");
		addFS("favorites", "labels/valuable");
		addFS("folder_blue", "ui/folder");
		addFS("folder_doc", "labels/folder-txt");
		addFS("folder_favorites", "labels/folder-favorites");
		addFS("folder_home", "ui/home");
		addFS("folder_html", "labels/folder-remote");
		addFS("folder_image", "labels/folder-image");
		addFS("folder_locked", "labels/folder-locked");
		addFS("folder_music", "labels/folder-sound");
		addFS("network_local", "ui/internet");
		addFS("trashcan_empty", "emptytrashcan");
		addFS("trashcan_full", "fulltrashcan");

		addMime("document", "notepad");
		addMime("empty", "ui/file");
		addMime("file_locked", "ui/password");
		addMime("image", "ui/image");
		addMime("unknown", "labels/unknown");

		setIconLoader(new KidsIconLoader(getName()));
		DefaultIconLoader.addUserIconLoader(getIconLoader());
	}

	// private

	private void addAction(final String old, final String makagiga) {
		map.put(makagiga + ".png", "actions/" + old + ".png");
	}

	private void addApp(final String old, final String makagiga) {
		map.put(makagiga + ".png", "apps/" + old + ".png");
	}

	private void addFS(final String old, final String makagiga) {
		map.put(makagiga + ".png", "filesystems/" + old + ".png");
	}

	private void addMime(final String old, final String makagiga) {
		map.put(makagiga + ".png", "mimetypes/" + old + ".png");
	}

	// private classes

	private final class KidsIconLoader extends DefaultIconLoader {

		// public
		
		@Override
		public MIcon load(String name, final int size) {
			if (name != null) {
				if (!name.endsWith(".png"))
					name += ".png";
				name = Plugin.this.map.get(name);
			}

			if (name == null)
				return null;

			return readIcon(
				Plugin.class,
				(size == MIcon.getSmallSize()) ? "icons/16x16" : "icons/48x48",
				name
			);
		}

		// private
		
		private KidsIconLoader(final String name) {
			super(name);
			setBaseSize(48);
		}
		
	}

}
