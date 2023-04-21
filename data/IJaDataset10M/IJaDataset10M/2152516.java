package com.diancai.cache;

import com.diancai.custom.TDiancai;
import java.util.Collection;

/**
 *
 * @author Zhong Fuqiang
 * @since 2012/2/6
 */
public interface IDiancaiCache {

    /**
     * 添加对象到缓存系统中
     *
     * @param entity
     */
    public void Create(String key, TDiancai value);

    /**
     * 更新缓存中的对象
     *
     * @param entity
     */
    public void Update(String key, TDiancai value);

    /**
     * 删除缓存中的对象
     *
     * @param entity
     */
    public void Delete(String key);

    /**
     * 根据key查询对象
     *
     * @param key
     * @return
     */
    public TDiancai FindByKey(String key);

    /**
     * 查出所有记录
     *
     * @return
     */
    public Collection<TDiancai> FindAll();
}
