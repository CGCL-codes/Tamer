package com.mgensystems.jarindexer.shell;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import com.mgensystems.jarindexer.model.ModelFactory;
import com.mgensystems.jarindexer.model.index.IndexModel;
import com.mgensystems.shell.AbortException;
import com.mgensystems.shell.Command;
import com.mgensystems.shell.Shell;

/**
 * <b>Title:</b> Command Helper<br />
 * <b>Description:</b> A command helper utiltiy. Mainly used to abstract out
 * retrieval of formatted/localized text for the commands.<br />
 * <b>Changes:</b><ol><li></li></ol>
 * 
 * @author raykroeker@gmail.com
 */
class CommandHelper {

    /** A resource bundle. */
    private final ResourceBundle bundle;

    /** A model factory. */
    private final ModelFactory modelFactory;

    /**
	 * Create CommandHelper.
	 * 
	 * @param bundle
	 *            A <code>ResourceBundle</code>.
	 * @param modelFactory
	 *            A <code>ModelFactory</code>.
	 */
    CommandHelper(final ResourceBundle bundle, final ModelFactory modelFactory) {
        super();
        this.bundle = bundle;
        this.modelFactory = modelFactory;
    }

    /**
	 * Prompt the user with a message looking for a y/n response. If the user
	 * does not reply with a specific y or n; this method will continue to
	 * prompt.
	 * 
	 * @param shell
	 *            A <code>Shell</code>.
	 * @param key
	 *            A <code>String</code>.
	 * @param arguments
	 *            An optional <code>Object[]</code>.
	 * @return A <code>boolean<code>.
	 * @throws IOException
	 *             if an io error occurs
	 */
    boolean confirm(final Shell shell, final String key, final Object... arguments) throws IOException {
        while (true) {
            final String confirm = shell.read(getString(key, arguments));
            if (null == confirm) {
                continue;
            }
            if (1 == confirm.length()) {
                if ('y' == confirm.charAt(0)) {
                    return true;
                } else if ('n' == confirm.charAt(0)) {
                    return false;
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
    }

    /**
	 * Obtain a formatted localized string.
	 * 
	 * @param key
	 *            A <code>String</code>.
	 * @param arguments
	 *            An optional <code>Object[]</code>.
	 * @return A <code>String</code>.
	 */
    String getString(final String key, final Object... arguments) {
        try {
            return MessageFormat.format(bundle.getString(key), arguments);
        } catch (final MissingResourceException mrx) {
            return null;
        }
    }

    /**
	 * Obtain a list of formatted localized strings.
	 * 
	 * @param key
	 *            A <code>String</code>.
	 * @param arguments
	 *            An optional <code>Object[]</code>.
	 * @return A <code>String</code>.
	 */
    List<String> getStrings(final String key, final String tokens, final Object... arguments) {
        try {
            final StringTokenizer tokenizer = new StringTokenizer(bundle.getString(key), tokens);
            final List<String> strings = new ArrayList<String>(tokenizer.countTokens());
            while (tokenizer.hasMoreTokens()) {
                strings.add(MessageFormat.format(tokenizer.nextToken(), arguments));
            }
            return strings;
        } catch (final MissingResourceException mrx) {
            return Collections.emptyList();
        }
    }

    /**
	 * Instantiate the index model.
	 * 
	 * @return An <code>IndexModel</code>.
	 */
    IndexModel newModel() {
        return modelFactory.newIndexModel();
    }

    /**
	 * Prompt the user for a named index. If the index does not exist; the user
	 * is prompted to retry. If no index an be located, an abort exception is
	 * thrown.
	 * 
	 * @param shell
	 *            A <code>Shell</code>.
	 * @param command
	 *            A <code>Command</code>.
	 * @return An <code>com.mgensystems.jarindexer.model.index.Index</code>.
	 * @throws IOException
	 *             if an io error occurs
	 * @throws AbortException
	 *             if the user decides to abort
	 */
    com.mgensystems.jarindexer.model.index.Index promptIndex(final Shell shell, final Command command) throws IOException, AbortException {
        while (true) {
            final String name = shell.read(getString("common.promptindex", readIndices()));
            final com.mgensystems.jarindexer.model.index.Index index = newModel().readIndex(name);
            if (null == index) {
                if (confirm(shell, "common.nosuchindex", name)) {
                    continue;
                } else {
                    throw new AbortException(shell, command);
                }
            } else {
                return index;
            }
        }
    }

    /**
	 * Read the available indices and place them in a list.
	 * 
	 * @return A <code>String</code>.
	 */
    private String readIndices() {
        final List<com.mgensystems.jarindexer.model.index.Index> list = newModel().readIndices();
        final StringBuilder indices = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (0 < i) {
                indices.append(',');
            }
            indices.append(list.get(i).getName());
        }
        return indices.toString();
    }
}
