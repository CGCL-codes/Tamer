package org.pojosoft.ria.gwt.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.pojosoft.ria.gwt.client.Event;
import org.pojosoft.ria.gwt.client.RecordLayoutEvent;
import org.pojosoft.ria.gwt.client.RecordLayoutListener;
import org.pojosoft.ria.gwt.client.meta.EventListenerMeta;
import org.pojosoft.ria.gwt.client.meta.LayoutMeta;
import org.pojosoft.ria.gwt.client.meta.RecordLayoutMeta;
import org.pojosoft.ria.gwt.client.service.ModelObjectServiceAsync;
import org.pojosoft.ria.gwt.client.service.SearchServiceAsync;
import org.pojosoft.ria.gwt.client.ui.support.ServiceLocator;
import java.util.Iterator;
import java.util.Map;

/**
 * Default listener implementation for {@link org.pojosoft.ria.gwt.client.RecordLayoutListener}
 *
 * @author POJO Software
 */
public class DefaultRecordLayoutListener implements RecordLayoutListener {

    protected Map params;

    public void setParams(Map params) {
        this.params = params;
    }

    public void addParam(Object key, Object param) {
        params.put(key, param);
    }

    public void onBeforeRender(RecordLayoutEvent.RenderEvent event) {
    }

    public void onAfterRender(RecordLayoutEvent.RenderEvent event) {
    }

    public boolean onBeforeAction(RecordLayoutEvent event) {
        return true;
    }

    public void onAction(RecordLayoutEvent event, AsyncCallback callback) {
        if (event instanceof RecordLayoutEvent.SearchModelObjectEvent) {
            onSearchModelObject((RecordLayoutEvent.SearchModelObjectEvent) event, callback);
        } else if (event instanceof RecordLayoutEvent.CreateModelObjectEvent) {
            onCreateModelObject((RecordLayoutEvent.CreateModelObjectEvent) event, callback);
        } else if (event instanceof RecordLayoutEvent.UpdateModelObjectEvent) {
            onUpdateModelObject((RecordLayoutEvent.UpdateModelObjectEvent) event, callback);
        } else if (event instanceof RecordLayoutEvent.RemoveModelObjectEvent) {
            onRemoveModelObject((RecordLayoutEvent.RemoveModelObjectEvent) event, callback);
        }
    }

    public void onAfterAction(RecordLayoutEvent event) {
    }

    void onSearchModelObject(RecordLayoutEvent.SearchModelObjectEvent event, final AsyncCallback callback) {
        SearchServiceAsync searchServiceAsync = ServiceLocator.lookupSearchService();
        searchServiceAsync.search(event.getModuleMeta().id, event.getLayoutId(), getQueryName(event), event.getSearchCriteria(), new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            public void onSuccess(Object object) {
                callback.onSuccess(object);
            }
        });
    }

    String getQueryName(RecordLayoutEvent.SearchModelObjectEvent event) {
        for (Iterator iterator = event.getModuleMeta().layouts.iterator(); iterator.hasNext(); ) {
            LayoutMeta layoutMeta = (LayoutMeta) iterator.next();
            if (layoutMeta.id.equals("Search")) {
                RecordLayoutMeta searchLayoutMeta = (RecordLayoutMeta) layoutMeta;
                if (searchLayoutMeta.renderer.listeners != null) {
                    for (Iterator iterator1 = searchLayoutMeta.renderer.listeners.iterator(); iterator1.hasNext(); ) {
                        EventListenerMeta eventListenerMeta = (EventListenerMeta) iterator1.next();
                        if (eventListenerMeta.name.equals("searchListener")) {
                            return (String) eventListenerMeta.params.get("queryName");
                        }
                    }
                }
            }
        }
        throw new RuntimeException("The queryName for the searchListener cannot be determined for module " + event.getModuleMeta().id + " using layout id Search");
    }

    void onCreateModelObject(RecordLayoutEvent.CreateModelObjectEvent event, final AsyncCallback callback) {
        ModelObjectServiceAsync modelObjectService = ServiceLocator.lookupModelObjectService();
        modelObjectService.addModelRow(event.getModelRow(), new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            public void onSuccess(Object object) {
                callback.onSuccess(object);
            }
        });
    }

    void onUpdateModelObject(RecordLayoutEvent.UpdateModelObjectEvent event, final AsyncCallback callback) {
        ModelObjectServiceAsync modelObjectService = ServiceLocator.lookupModelObjectService();
        modelObjectService.updateModelRow(event.getModelRow(), new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            public void onSuccess(Object object) {
                callback.onSuccess(object);
            }
        });
    }

    void onRemoveModelObject(RecordLayoutEvent.RemoveModelObjectEvent event, final AsyncCallback callback) {
        ModelObjectServiceAsync modelObjectService = ServiceLocator.lookupModelObjectService();
        modelObjectService.removeModelRow(event.getModelObjectName(), event.getModelObjectId(), new AsyncCallback() {

            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            public void onSuccess(Object object) {
                callback.onSuccess(object);
            }
        });
    }

    public void onEvent(Event event, AsyncCallback callback) {
    }
}
