package br.com.sysmap.crux.core.client.datasource;

/**
 * @author Thiago da Rosa de Bustamante
 * @deprecated Use RemotePagedDataSource instead
 */
@Deprecated
public abstract class RemoteEditablePagedDataSource<T> extends RemotePagedDataSource<T> {

    /**
	 * @see br.com.sysmap.crux.core.client.datasource.DataSource#getBoundObject()
	 */
    public T getBoundObject() {
        return super.getBoundObject(getRecord());
    }

    /**
	 * @see br.com.sysmap.crux.core.client.datasource.DataSource#getBoundObject(br.com.sysmap.crux.core.client.datasource.DataSourceRecord)
	 */
    public T getBoundObject(DataSourceRecord<T> record) {
        return super.getBoundObject(record);
    }
}
