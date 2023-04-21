
package importtheme;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.makagiga.commons.Args;
import org.makagiga.commons.FS;
import org.makagiga.commons.MApplication;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.icons.IconTheme;
import org.makagiga.commons.io.Checksum;
import org.makagiga.commons.io.FileScanner;

/**
 * Internal tool. Do not use!
 *
 * @since 2.0
 */
public final class Main extends MApplication {

	// private

	private static final Set<String> skipSet = new HashSet<>();

	// public
	
	public static void main(final String... args) throws Exception {
		init(args, "makagiga-import-theme");
		MLogger.developer.set(true);

		skipSet.add("labels/flag-blue");
		skipSet.add("labels/todo");
		skipSet.add("text/bold");
		skipSet.add("text/italic");
		skipSet.add("text/strikethrough");
		skipSet.add("text/underline");
		skipSet.add("ui/close");
		
		File baseSize = new File(Args.getOption("base-size"));
		if (!baseSize.exists()) {
			MLogger.error("core", "Source directory does not exist: %s", baseSize);
			System.exit(1);
		}

		File output = new File(Args.getOption("output"));
		if (!output.exists()) {
			MLogger.error("core", "Destination directory does not exist: %s", output);
			System.exit(1);
		}

		final TreeSet<File> unmappedFiles = new TreeSet<>();
		new FileScanner.Simple(output) {
			@Override
			public void processFile(final File file) throws Exception {
				if (!file.getPath().contains(".svn"))
					unmappedFiles.add(file);
			}
		};

		update(unmappedFiles, baseSize, output);
		
		File smallSize = new File(Args.getOption("small-size"));
		if (smallSize.exists()) {
			update(unmappedFiles, smallSize, new File(output, "small"));
		}
		
		if (!unmappedFiles.isEmpty()) {
			MLogger.warning("core", "Unmapped files:");
			for (File i : unmappedFiles)
				MLogger.warning("core", "\t" + i);
		}
	}

	// protected
	
	@Override
	protected void startup() { }

	// private

	private static boolean skipName(final String name) {
		if (skipSet.contains(name)) {
			MLogger.warning("core", "Skipping icon update: %s", name);

			return true;
		}

		return false;
	}

	private static void update(final Set<File> unmappedFiles, final File source, final File output) throws Exception {
		output.mkdirs();

		Map<String, List<String>> aliases = IconTheme.getAliases();
		for (Map.Entry<String, String> i : IconTheme.getToMakagigaMap().entrySet()) {
			if (skipName(i.getValue()))
				continue; // for

			File sourceFile = new File(source, i.getKey() + ".png");

			if (!sourceFile.exists()) {
				MLogger.warning("core", "Source file does not exist: %s", sourceFile);

				continue; // for
			}

			File destFile = new File(output, i.getValue() + ".png");
			updateFile(sourceFile, destFile);
			unmappedFiles.remove(destFile);

			List<String> aliasList = aliases.get(i.getKey());
			if (aliasList != null) {
				for (String alias : aliasList) {
					destFile = new File(output, alias + ".png");
					updateFile(sourceFile, destFile);
					unmappedFiles.remove(destFile);
				}
			}
		}
	}
	
	private static void updateFile(final File sourceFile, final File destinationFile) throws Exception {
		if (destinationFile.exists() && Arrays.equals(
			Checksum.get("md5", sourceFile),
			Checksum.get("md5", destinationFile)
		))
			return;
			
		MLogger.info("core", "%s -> %s", sourceFile, destinationFile);
		destinationFile.getParentFile().mkdirs();
		FS.copyFile(sourceFile, destinationFile);
	}
	
}
