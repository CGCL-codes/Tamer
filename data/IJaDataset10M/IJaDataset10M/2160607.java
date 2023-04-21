package self.micromagic.eterna.sql;

import self.micromagic.eterna.digester.ConfigurationException;

public interface UpdateAdapterGenerator extends SQLAdapterGenerator {

    /**
    * ���һ��<code>QueryAdapter</code>��ʵ��. <p>
    *
    * @return <code>QueryAdapter</code>��ʵ��.
    * @throws ConfigurationException     ��������ó���ʱ.
    */
    UpdateAdapter createUpdateAdapter() throws ConfigurationException;
}
