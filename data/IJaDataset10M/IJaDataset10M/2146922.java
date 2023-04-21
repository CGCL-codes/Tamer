package com.bukkit.epicsaga.EpicZones;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;

public class EpicZonesEntityListener extends EntityListener {

    private final EpicZones plugin;

    public EpicZonesEntityListener(EpicZones instance) {
        plugin = instance;
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getCause() == DamageCause.ENTITY_ATTACK) {
            if (isPlayer(event.getEntity()) && isPlayer(event.getDamager())) {
                EpicZonePlayer ezp = General.getPlayer(event.getEntity().getEntityId());
                EpicZone zone = ezp.getCurrentZone();
                if (zone != null) {
                    if (zone.getFlags().get("pvp") != null) {
                        if (!zone.getFlags().get("pvp")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEntityDamageByProjectile(EntityDamageByProjectileEvent event) {
        if (event.getCause() == DamageCause.ENTITY_ATTACK) {
            if (isPlayer(event.getEntity()) && isPlayer(event.getDamager())) {
                EpicZonePlayer ezp = General.getPlayer(event.getEntity().getEntityId());
                EpicZone zone = ezp.getCurrentZone();
                if (zone != null) {
                    if (zone.getFlags().get("pvp") != null) {
                        if (!zone.getFlags().get("pvp")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean isPlayer(Entity entity) {
        boolean result = false;
        if (General.getPlayer(entity.getEntityId()) != null) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}
