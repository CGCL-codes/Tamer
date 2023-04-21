package net.sf.buildbox.devportal.client.fake;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import java.util.*;
import net.sf.buildbox.devmodel.*;
import net.sf.buildbox.devmodel.ui.client.HttpHelper;
import net.sf.buildbox.devmodel.ui.client.Misc;
import net.sf.buildbox.devmodel.ui.client.XmlHelper;
import net.sf.buildbox.devportal.client.model.AsyncVersion;

public class ModuleCache {

    private final String baseurl;

    public final Map<VcsLocation, AsyncVersion> cachedVersions = new HashMap<VcsLocation, AsyncVersion>();

    public ModuleCache(String baseurl) {
        this.baseurl = baseurl;
    }

    public void read(final VcsLocation loc, final AsyncCallback<ModuleCache> callback) {
        final String url = baseurl + "/" + loc.getDomainId() + "/" + loc.getRepoId() + "/" + loc.getLocationPath().replaceAll("/", "_") + ".version.xml";
        HttpHelper.httpGetXml(url, "http://buildbox.sf.net/devmodel", "modules", new AsyncCallback<Element>() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(Element result) {
                try {
                    final Element firstElem = (Element) result.getElementsByTagName("module").item(0);
                    final AsyncVersion module = parseModule(firstElem, loc);
                    cachedVersions.put(loc, module);
                    callback.onSuccess(ModuleCache.this);
                } catch (Throwable e) {
                    callback.onFailure(e);
                }
            }
        });
    }

    private AsyncVersion parseModule(Element moduleElem, VcsLocation loc) {
        final VersionId moduleId = VersionId.create(moduleElem.getAttribute("groupId"), moduleElem.getAttribute("artifactId"), moduleElem.getAttribute("version"));
        final VersionId predecessor;
        {
            final NodeList predecessorNodes = moduleElem.getElementsByTagName("predecessor");
            if (predecessorNodes.getLength() > 0) {
                final Element elem = (Element) predecessorNodes.item(0);
                predecessor = VersionId.create(elem.getAttribute("groupId"), elem.getAttribute("artifactId"), elem.getAttribute("version"));
            } else {
                predecessor = null;
            }
        }
        final List<Dependency> dependencies = new ArrayList<Dependency>();
        {
            final NodeList dependencyNodes = moduleElem.getElementsByTagName("dependency");
            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                final Element elem = (Element) dependencyNodes.item(i);
                final VersionId vid = VersionId.create(elem.getAttribute("groupId"), elem.getAttribute("artifactId"), elem.getAttribute("version"));
                final ArtifactRef a = ArtifactRef.create(vid, elem.getAttribute("classifier"), elem.getAttribute("type"));
                final NodeList exclusionNodes = elem.getElementsByTagName("exclusion");
                final Set<SoftwareName> exclusions = new LinkedHashSet<SoftwareName>();
                for (int ii = 0; ii < exclusionNodes.getLength(); ii++) {
                    final Element excElem = (Element) exclusionNodes.item(ii);
                    exclusions.add(SoftwareName.create(excElem.getAttribute("groupId"), excElem.getAttribute("artifactId")));
                }
                final NodeList propertyNodes = elem.getElementsByTagName("property");
                final Map<String, String> properties = new LinkedHashMap<String, String>();
                for (int ii = 0; ii < propertyNodes.getLength(); ii++) {
                    final Element propElem = (Element) propertyNodes.item(ii);
                    properties.put(propElem.getAttribute("name"), XmlHelper.getElementText(propElem));
                }
                dependencies.add(Dependency.create(a, exclusions, properties));
            }
        }
        final List<DeclaredChange> declaredChanges = new LinkedList<DeclaredChange>();
        {
            final NodeList changeNodes = moduleElem.getElementsByTagName("change");
            for (int i = 0; i < changeNodes.getLength(); i++) {
                final Element elem = (Element) changeNodes.item(i);
                final String timeStr = elem.getAttribute("time");
                final Date time = timeStr == null ? null : Misc.ISO_DATETIME_FORMAT.parse(timeStr);
                final DeclaredChangeType changeType = DeclaredChangeType.valueOf(elem.getAttribute("action"));
                declaredChanges.add(new DeclaredChange(time, changeType, elem.getAttribute("issue"), elem.getAttribute("component"), elem.getAttribute("author"), XmlHelper.getElementText(elem)));
            }
        }
        final List<Hint> hints = new LinkedList<Hint>();
        {
            final NodeList hintNodes = moduleElem.getElementsByTagName("hint");
            for (int i = 0; i < hintNodes.getLength(); i++) {
                final Element elem = (Element) hintNodes.item(i);
                final Hint hint = new Hint();
                hint.setSeverity(HintSeverity.valueOf(elem.getAttribute("severity")));
                hint.setCode(elem.getAttribute("code"));
                hint.setDescription(XmlHelper.getElementText(elem));
                hints.add(hint);
            }
        }
        final Set<ArtifactRef> outputs = new LinkedHashSet<ArtifactRef>();
        {
            final NodeList outputNodes = moduleElem.getElementsByTagName("output");
            for (int i = 0; i < outputNodes.getLength(); i++) {
                final Element elem = (Element) outputNodes.item(i);
                final String groupId = elem.getAttribute("groupId");
                final String artifactId = elem.getAttribute("artifactId");
                final String version = elem.getAttribute("version");
                final VersionId vid = VersionId.create(groupId == null ? moduleId.getGroupId() : groupId, artifactId == null ? moduleId.getArtifactId() : artifactId, version == null ? moduleId.getVersion() : version);
                final String classifier = elem.getAttribute("classifier");
                final String type = elem.getAttribute("type");
                outputs.add(ArtifactRef.create(vid, classifier, type));
            }
        }
        final Map<String, List<String>> properties = new HashMap<String, List<String>>();
        {
            final NodeList propertyNodes = moduleElem.getElementsByTagName("property");
            for (int i = 0; i < propertyNodes.getLength(); i++) {
                final Element elem = (Element) propertyNodes.item(i);
                final String propertyName = elem.getAttribute("name");
                final List<String> items = new ArrayList<String>();
                final NodeList itemNodes = elem.getElementsByTagName("item");
                for (int ii = 0; ii < itemNodes.getLength(); ii++) {
                    final Element itemElem = (Element) itemNodes.item(ii);
                    items.add(XmlHelper.getElementText(itemElem));
                }
                if (!items.isEmpty()) {
                    properties.put(propertyName, items);
                }
            }
        }
        return new AsyncVersion(moduleId, loc, predecessor, dependencies, declaredChanges, hints, properties, outputs);
    }
}
