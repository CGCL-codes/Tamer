package com.germinus.xpression.cms.ebooks;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import org.apache.tika.io.ByteArrayOutputStream;
import com.germinus.xpression.cms.FileUtil;
import com.germinus.xpression.cms.ebooks.OpfPackage.Item;

public class EpubToolMenu {

    public static class MergedItem {

        private static final String BASE_XHTML = "<?xml version='1.0' encoding='utf-8'?>\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n";

        private static final String END_XHTML = "\n</body>\n</html>";

        private Item baseItem;

        List<Item> mergeItems;

        private File mergedFile;

        private String label;

        public MergedItem(Item baseItem, NavPoint navPoint) {
            this.baseItem = baseItem;
            this.mergedFile = baseItem.getFullPath();
            this.label = navPoint.getLabel();
        }

        public void mergeWith(Item item) throws IOException {
            if (mergeItems == null) {
                mergeItems = new ArrayList<Item>();
                try {
                    mergedFile = File.createTempFile(baseItem.id + "merged", ".html", baseItem.getFullPath().getParentFile());
                } catch (IOException e) {
                    throw new IOException("Cannot create temp file for doing item merge", e);
                }
            }
            this.mergeItems.add(item);
        }

        public File getAbsoluteFile() throws IOException {
            if (mergeItems != null) {
                Source parsedItem = parseItem(baseItem);
                String baseBody = bodyFromSource(parsedItem);
                String head = headFromSource(parsedItem);
                StringBuilder fullFile = new StringBuilder(BASE_XHTML);
                fullFile.append(head);
                fullFile.append(baseBody);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                FileUtil.dumpInputStreamToOutputStream(new FileInputStream(baseItem.getFullPath()), os);
                for (Item mergeItem : mergeItems) {
                    fullFile.append(bodyFromItem(mergeItem));
                }
                fullFile.append(END_XHTML);
                FileUtil.dumpInputStreamToOutputStream(new ByteArrayInputStream(fullFile.toString().getBytes("UTF-8")), new FileOutputStream(mergedFile), true);
                return mergedFile;
            } else return baseItem.getFullPath();
        }

        private String bodyFromItem(Item item) throws IOException, FileNotFoundException {
            Source parseItem = parseItem(item);
            return bodyFromSource(parseItem);
        }

        private String bodyFromSource(Source parseItem) {
            Element bodyElement = parseItem.getAllElements("body").get(0);
            return bodyElement.getContent().toString();
        }

        private String headFromSource(Source parseItem) {
            Element bodyElement = parseItem.getAllElements("head").get(0);
            return bodyElement.getContent().toString();
        }

        private Source parseItem(Item item) throws IOException, FileNotFoundException {
            return new Source(new FileReader(item.getFullPath()));
        }

        public String getLabel() {
            return label;
        }
    }

    private OpfPackage opfPackage;

    private List<MergedItem> mergedItems;

    public EpubToolMenu(Epub epub) throws IOException {
        this.opfPackage = epub.getOpfPackage();
        this.mergedItems = mergeItems(opfPackage.getSpineItems(), opfPackage.getParsedToc());
        for (MergedItem mergedItem : mergedItems) mergedItem.getAbsoluteFile();
    }

    private List<MergedItem> mergeItems(List<Item> spineItems, SortedMap<Integer, NavPoint> parsedToc) throws IOException {
        List<MergedItem> mergedItems = new ArrayList<MergedItem>(parsedToc.size());
        Iterator<NavPoint> tocIterator = parsedToc.values().iterator();
        while (tocIterator.hasNext()) {
            NavPoint nextNavPoint = tocIterator.next();
            oneSpineRound(spineItems, mergedItems, tocIterator, nextNavPoint);
        }
        return mergedItems;
    }

    private void oneSpineRound(List<Item> spineItems, List<MergedItem> mergedItems, Iterator<NavPoint> tocIterator, NavPoint nextNavPoint) throws IOException {
        String nextNavPointHref = nextNavPoint.getContentRelativePath();
        if (nextNavPointHref.contains("#")) {
            nextNavPointHref = nextNavPointHref.substring(0, nextNavPointHref.indexOf('#'));
        }
        int spineIndex = 0;
        MergedItem lastTocItem = null;
        while (spineIndex < spineItems.size()) {
            Item currentSpineItem = spineItems.get(spineIndex++);
            boolean mergeHtml = lastTocItem != null;
            if (nextNavPoint != null) {
                if (currentSpineItem.href.equals(nextNavPointHref)) {
                    mergeHtml = false;
                    lastTocItem = new MergedItem(currentSpineItem, nextNavPoint);
                    mergedItems.add(lastTocItem);
                    if (tocIterator.hasNext()) {
                        nextNavPoint = tocIterator.next();
                        nextNavPointHref = nextNavPoint.getContentRelativePath();
                        if (nextNavPointHref.contains("#")) {
                            nextNavPointHref = nextNavPointHref.substring(0, nextNavPointHref.indexOf('#'));
                        }
                    } else nextNavPoint = null;
                }
            }
            if (mergeHtml && currentSpineItem.isLinear()) {
                lastTocItem.mergeWith(currentSpineItem);
            }
        }
    }

    public List<MergedItem> navigation() {
        return mergedItems;
    }
}
