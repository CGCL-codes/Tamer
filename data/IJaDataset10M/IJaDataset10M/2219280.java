package com.foo.datasource;

import com.foo.dao.MusteriDAO;
import com.foo.data.Musteri;
import com.foo.data.Rol;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Altug Bilgin ALTINTAS
 */
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MusteriDAO musteriDao;

    public UserDetails loadUserByUsername(String eposta) throws UsernameNotFoundException, DataAccessException {
        Musteri musteri = musteriDao.bul(eposta);
        if (musteri != null) {
            ArrayList<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
            List<Rol> roller = musteri.getRolListesi();
            for (Rol rol : roller) {
                ga.add(new GrantedAuthorityImpl(rol.getRolIsmi()));
            }
            GrantedAuthority[] grantedAuthorities = new GrantedAuthority[ga.size()];
            ga.toArray(grantedAuthorities);
            MyUserDetails myUserDetails = new MyUserDetails(eposta, musteri.getSifre(), true, true, true, true, grantedAuthorities);
            return myUserDetails;
        } else {
            throw new UsernameNotFoundException(eposta);
        }
    }
}
