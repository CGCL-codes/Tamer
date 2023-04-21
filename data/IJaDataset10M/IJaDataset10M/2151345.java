package org.jholbrook.buddi.plugins.imports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.homeunix.thecave.buddi.plugin.api.exception.ModelException;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableTransaction;
import org.homeunix.thecave.buddi.plugin.api.model.MutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.MutableDocument;
import org.homeunix.thecave.buddi.plugin.api.model.MutableModelFactory;
import org.homeunix.thecave.buddi.plugin.api.model.MutableSource;
import org.homeunix.thecave.buddi.plugin.api.model.MutableTransaction;

public class QifImporterLevel2 extends AbstractQIFImporter {

    public void insertQifs(List<QIFRecord> qifRecords, MutableDocument model, MutableAccount account) throws ModelException {
        Map<String, ImmutableSource> sourceMapping = buildStats(model, account);
        Collection<String> existingKeys = getTransactionKeys(model, account);
        for (QIFRecord qif : qifRecords) {
            if (existingKeys.contains(qif.getCheckNo())) {
                continue;
            }
            MutableSource from;
            MutableSource to;
            MutableSource cat;
            if (sourceMapping.containsKey(qif.getDescription())) {
                cat = (MutableSource) sourceMapping.get(qif.getDescription());
            } else {
                cat = getUnclassifiedBudgetCategory(model, qif.getAmount() >= 0);
            }
            if (qif.getAmount() >= 0) {
                from = cat;
                to = account;
            } else {
                from = account;
                to = cat;
            }
            MutableTransaction transaction = MutableModelFactory.createMutableTransaction(qif.getDate(), qif.getDescription(), Math.abs(qif.getAmount()), from, to);
            transaction.setNumber(qif.getCheckNo());
            transaction.setMemo(qif.getMemo());
            if (transaction.getDescription() == null) {
                transaction.setDescription("");
            }
            model.addTransaction(transaction);
        }
    }

    private Collection<String> getTransactionKeys(MutableDocument model, ImmutableSource account) {
        Collection<String> retval = new HashSet<String>();
        Calendar sixMonthsAgo = Calendar.getInstance();
        sixMonthsAgo.add(Calendar.MONTH, -6);
        List<ImmutableTransaction> transactions = model.getImmutableTransactions(account);
        for (ImmutableTransaction transaction : transactions) {
            retval.add(transaction.getNumber());
        }
        return retval;
    }

    /**
	 * Analyse the Existing transactions and analyse, which ImmutableSource is
	 * the most probable one for each description used in any transaction
	 * 
	 * @param syncQifPlugin
	 *            TODO
	 * @param model
	 * @param account
	 *            TODO
	 * @return
	 */
    public Map<String, ImmutableSource> buildStats(MutableDocument model, ImmutableSource account) {
        Map<String, ImmutableSource> retval = new HashMap<String, ImmutableSource>();
        Map<String, List<ImmutableTransaction>> transactBySourceDesc = new HashMap<String, List<ImmutableTransaction>>();
        Calendar sixMonthsAgo = Calendar.getInstance();
        sixMonthsAgo.add(Calendar.MONTH, -6);
        List<ImmutableTransaction> transactions = model.getImmutableTransactions(account, sixMonthsAgo.getTime(), new Date());
        for (ImmutableTransaction transaction : transactions) {
            List<ImmutableTransaction> sources = transactBySourceDesc.get(transaction.getDescription());
            if (sources == null) {
                sources = new ArrayList<ImmutableTransaction>();
                transactBySourceDesc.put(transaction.getDescription(), sources);
            }
            sources.add(transaction);
        }
        for (String sourceDesc : transactBySourceDesc.keySet()) {
            ImmutableSource mostCommon = buildStatForKey(transactBySourceDesc.get(sourceDesc));
            retval.put(sourceDesc, mostCommon);
        }
        return retval;
    }

    /**
	 * Analyse which ImmutableSource is the most probable one for the given
	 * source Description
	 * 
	 * @param sourceDesc
	 * @param transactions
	 *            All Transactions with the same description
	 * @return
	 */
    private ImmutableSource buildStatForKey(List<ImmutableTransaction> transactions) {
        Collections.sort(transactions, new Comparator<ImmutableTransaction>() {

            public int compare(ImmutableTransaction me, ImmutableTransaction other) {
                return me.getDate().compareTo(other.getDate());
            }
        });
        Map<ImmutableSource, Integer> occurrences = new HashMap<ImmutableSource, Integer>();
        for (int i = 0; i < Math.min(10, transactions.size()); i++) {
            ImmutableTransaction transaction = transactions.get(i);
            ImmutableSource source;
            if (transaction.isInflow()) source = transaction.getFrom(); else source = transaction.getTo();
            Integer occurrence = occurrences.get(source);
            if (occurrence == null) occurrence = 0;
            occurrences.put(source, occurrence + 1);
        }
        ImmutableSource mostCommon = null;
        Integer mostCommonCount = 0;
        for (ImmutableSource source : occurrences.keySet()) {
            if (occurrences.get(source) > mostCommonCount) {
                mostCommonCount = occurrences.get(source);
                mostCommon = source;
            }
        }
        return mostCommon;
    }
}
